package com.example.electrical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.electrical.common.Constants;
import com.example.electrical.common.Result;
import com.example.electrical.entity.ConsumptionRecord;
import com.example.electrical.entity.ElectricityUsage;
import com.example.electrical.entity.PaymentBill;
import com.example.electrical.entity.User;
import com.example.electrical.entity.UserAccount;
import com.example.electrical.mapper.ConsumptionRecordMapper;
import com.example.electrical.mapper.ElectricityUsageMapper;
import com.example.electrical.mapper.PaymentBillMapper;
import com.example.electrical.mapper.UserAccountMapper;
import com.example.electrical.mapper.UserMapper;
import com.example.electrical.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentBillMapper, PaymentBill> implements PaymentService {

    @Autowired
    private PaymentBillMapper paymentBillMapper;

    @Autowired
    private ElectricityUsageMapper electricityUsageMapper;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private ConsumptionRecordMapper consumptionRecordMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<List<PaymentBill>> getBillList(Long studentId) {
        List<PaymentBill> list = paymentBillMapper.selectByStudentId(studentId);
        return Result.success(list);
    }

    @Override
    public Result<PaymentBill> getBillDetail(Long billId) {
        PaymentBill bill = paymentBillMapper.selectById(billId);
        if (bill == null) {
            return Result.error("账单不存在");
        }
        return Result.success(bill);
    }

    @Override
    @Transactional
    public Result<Void> payBill(Long studentId, Long usageId) {
        // 检查用电记录
        ElectricityUsage usage = electricityUsageMapper.selectById(usageId);
        if (usage == null) {
            return Result.error("用电记录不存在");
        }

        // 检查是否有权操作该记录：1. 是记录的学生本人 2. 是同宿舍的学生
        if (!usage.getStudentId().equals(studentId)) {
            // 检查是否同宿舍
            User user = userMapper.selectById(studentId);
            if (user == null || user.getDormitoryId() == null || !user.getDormitoryId().equals(usage.getDormitoryId())) {
                return Result.error("无权操作该记录");
            }
        }

        if (usage.getStatus() == Constants.ELECTRICITY_STATUS_PAID) {
            return Result.error("该记录已缴费");
        }

        // 获取缴费金额
        // 如果balance为负数（欠费），则缴纳欠费金额；否则缴纳amount
        BigDecimal payAmount;
        if (usage.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            // 欠费情况：缴纳欠费金额的绝对值
            payAmount = usage.getBalance().abs();
        } else {
            // 正常情况：缴纳amount
            payAmount = usage.getAmount();
        }

        // 检查账户余额
        UserAccount account = userAccountMapper.selectByStudentId(studentId);
        if (account == null || account.getBalance().compareTo(payAmount) < 0) {
            return Result.error("账户余额不足，请先充值");
        }

        // 扣除账户余额
        int deductResult = userAccountMapper.deductBalance(studentId, payAmount);
        if (deductResult == 0) {
            return Result.error("扣费失败，请重试");
        }

        // 生成账单编号
        String billNo = "BILL" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + usageId;

        // 创建缴费单
        PaymentBill bill = new PaymentBill();
        bill.setBillNo(billNo);
        bill.setStudentId(studentId);
        bill.setUsageId(usageId);
        bill.setAmount(payAmount);
        bill.setStatus(Constants.PAYMENT_STATUS_PAID);
        bill.setPaymentTime(LocalDateTime.now());
        bill.setPaymentMethod("账户余额支付");

        paymentBillMapper.insert(bill);

        // 更新用电记录状态
        usage.setStatus(Constants.ELECTRICITY_STATUS_PAID);
        usage.setBalance(BigDecimal.ZERO);
        electricityUsageMapper.updateById(usage);

        // 创建消费记录
        ConsumptionRecord record = new ConsumptionRecord();
        record.setStudentId(studentId);
        record.setConsumptionNo("CNS" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + UUID.randomUUID().toString().substring(0, 6));
        record.setAmount(payAmount);
        // 根据用电记录类型判断消费类型
        if (usage.getUsageAmount().compareTo(BigDecimal.ZERO) == 0) {
            record.setType(Constants.CONSUMPTION_TYPE_LOGIN);
            record.setDescription("登录扣费补缴");
        } else {
            record.setType(Constants.CONSUMPTION_TYPE_ELECTRICITY);
            record.setDescription("电费缴纳");
        }
        record.setBalanceBefore(account.getBalance());
        record.setBalanceAfter(account.getBalance().subtract(payAmount));
        consumptionRecordMapper.insert(record);

        return Result.success();
    }

    @Override
    public Result<List<PaymentBill>> getAllBills() {
        List<PaymentBill> list = paymentBillMapper.selectList(null);
        return Result.success(list);
    }
}
