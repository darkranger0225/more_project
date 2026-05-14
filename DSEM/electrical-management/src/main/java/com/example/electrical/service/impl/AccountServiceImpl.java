package com.example.electrical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.electrical.common.Constants;
import com.example.electrical.common.Result;
import com.example.electrical.dto.LoginDeductionDTO;
import com.example.electrical.entity.ConsumptionRecord;
import com.example.electrical.entity.Dormitory;
import com.example.electrical.entity.ElectricityUsage;
import com.example.electrical.entity.PaymentBill;
import com.example.electrical.entity.RechargeRecord;
import com.example.electrical.entity.SystemConfig;
import com.example.electrical.entity.User;
import com.example.electrical.entity.UserAccount;
import com.example.electrical.mapper.ConsumptionRecordMapper;
import com.example.electrical.mapper.DormitoryMapper;
import com.example.electrical.mapper.ElectricityUsageMapper;
import com.example.electrical.mapper.PaymentBillMapper;
import com.example.electrical.mapper.RechargeRecordMapper;
import com.example.electrical.mapper.SystemConfigMapper;
import com.example.electrical.mapper.UserAccountMapper;
import com.example.electrical.mapper.UserMapper;
import com.example.electrical.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class AccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements AccountService {

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;

    @Autowired
    private ConsumptionRecordMapper consumptionRecordMapper;

    @Autowired
    private ElectricityUsageMapper electricityUsageMapper;

    @Autowired
    private PaymentBillMapper paymentBillMapper;

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Autowired
    private DormitoryMapper dormitoryMapper;

    @Override
    public Result<UserAccount> getAccountInfo(Long studentId) {
        UserAccount account = userAccountMapper.selectByStudentId(studentId);
        if (account == null) {
            // 创建新账户
            account = new UserAccount();
            account.setStudentId(studentId);
            account.setBalance(BigDecimal.ZERO);
            account.setTotalRecharge(BigDecimal.ZERO);
            account.setTotalConsumption(BigDecimal.ZERO);
            userAccountMapper.insert(account);
        }
        return Result.success(account);
    }

    @Override
    @Transactional
    public Result<String> createRechargeOrder(Long studentId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return Result.error("充值金额必须大于0");
        }

        // 生成充值编号
        String rechargeNo = "RCG" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + UUID.randomUUID().toString().substring(0, 6);

        // 获取当前账户
        UserAccount account = userAccountMapper.selectByStudentId(studentId);
        BigDecimal balanceBefore = account != null ? account.getBalance() : BigDecimal.ZERO;

        // 创建充值记录（状态为待支付）
        RechargeRecord record = new RechargeRecord();
        record.setStudentId(studentId);
        record.setRechargeNo(rechargeNo);
        record.setAmount(amount);
        record.setBalanceBefore(balanceBefore);
        record.setBalanceAfter(balanceBefore); // 待支付时，balance_after 先设为 balance_before，确认时再更新
        record.setStatus(0); // 0-待支付
        rechargeRecordMapper.insert(record);

        return Result.success(rechargeNo);
    }

    @Override
    @Transactional
    public Result<Void> confirmRecharge(String rechargeNo) {
        RechargeRecord record = rechargeRecordMapper.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<RechargeRecord>()
                .eq(RechargeRecord::getRechargeNo, rechargeNo)
        );

        if (record == null) {
            return Result.error("充值订单不存在");
        }

        if (record.getStatus() == 1) {
            return Result.error("该订单已充值成功");
        }

        // 更新充值记录状态
        record.setStatus(1);
        record.setBalanceAfter(record.getBalanceBefore().add(record.getAmount()));
        rechargeRecordMapper.updateById(record);

        // 更新账户余额
        UserAccount account = userAccountMapper.selectByStudentId(record.getStudentId());
        if (account == null) {
            // 创建新账户
            account = new UserAccount();
            account.setStudentId(record.getStudentId());
            account.setBalance(record.getAmount());
            account.setTotalRecharge(record.getAmount());
            account.setTotalConsumption(BigDecimal.ZERO);
            userAccountMapper.insert(account);
        } else {
            userAccountMapper.addBalance(record.getStudentId(), record.getAmount());
        }

        return Result.success();
    }

    @Override
    public Result<List<RechargeRecord>> getRechargeRecords(Long studentId) {
        List<RechargeRecord> list = rechargeRecordMapper.selectByStudentId(studentId);
        return Result.success(list);
    }

    @Override
    public Result<List<ConsumptionRecord>> getConsumptionRecords(Long studentId) {
        List<ConsumptionRecord> list = consumptionRecordMapper.selectByStudentId(studentId);
        return Result.success(list);
    }

    @Override
    @Transactional
    public Result<LoginDeductionDTO> deductForLogin(Long studentId) {
        // 生成随机用电量：5-15度
        Random random = new Random();
        int electricityUsage = 5 + random.nextInt(11); // 5-15度
        
        // 获取当前电价，默认0.60元/度
        BigDecimal electricityPrice = getElectricityPrice();
        
        // 计算费用，保留两位小数
        BigDecimal loginFee = electricityPrice.multiply(new BigDecimal(electricityUsage)).setScale(2, RoundingMode.HALF_UP);

        // 获取用户信息（包含宿舍ID）
        User user = userMapper.selectById(studentId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        Long dormitoryId = user.getDormitoryId();

        // 检查账户余额
        UserAccount account = userAccountMapper.selectByStudentId(studentId);
        if (account == null) {
            // 创建新账户
            account = new UserAccount();
            account.setStudentId(studentId);
            account.setBalance(BigDecimal.ZERO);
            account.setTotalRecharge(BigDecimal.ZERO);
            account.setTotalConsumption(BigDecimal.ZERO);
            userAccountMapper.insert(account);
        }

        LoginDeductionDTO dto = new LoginDeductionDTO();
        dto.setElectricityUsage(new BigDecimal(electricityUsage));
        dto.setAmount(loginFee);

        if (account.getBalance().compareTo(loginFee) >= 0) {
            // 余额充足，正常扣费
            int result = userAccountMapper.deductBalance(studentId, loginFee);
            if (result == 0) {
                dto.setSuccess(false);
                dto.setMessage("扣费失败");
                return Result.success(dto);
            }

            // 创建用电记录（已缴费状态）
            ElectricityUsage usage = new ElectricityUsage();
            usage.setStudentId(studentId);
            usage.setFloorId(0L); // 登录扣费没有楼层
            usage.setDormitoryId(dormitoryId); // 关联宿舍
            usage.setUsageAmount(new BigDecimal(electricityUsage)); // 实际用电度数
            usage.setAmount(loginFee); // 应缴金额
            usage.setBalance(BigDecimal.ZERO); // 已缴清
            usage.setRecordTime(LocalDateTime.now());
            usage.setStatus(Constants.ELECTRICITY_STATUS_PAID); // 已缴费
            electricityUsageMapper.insert(usage);

            // 获取宿舍信息用于remark字段
            String dormitoryInfo = "";
            if (dormitoryId != null) {
                Dormitory dormitory = dormitoryMapper.selectById(dormitoryId);
                if (dormitory != null) {
                    dormitoryInfo = dormitory.getBuildingId() + "栋-" + dormitory.getDormitoryNumber();
                }
            }

            // 创建缴费单
            String billNo = "BILL" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + usage.getId();
            PaymentBill bill = new PaymentBill();
            bill.setBillNo(billNo);
            bill.setStudentId(studentId);
            bill.setUsageId(usage.getId());
            bill.setAmount(loginFee);
            bill.setStatus(Constants.PAYMENT_STATUS_PAID);
            bill.setPaymentTime(LocalDateTime.now());
            bill.setPaymentMethod("账户余额自动扣费");
            bill.setRemark(dormitoryInfo); // 记录宿舍信息
            paymentBillMapper.insert(bill);

            // 创建消费记录
            ConsumptionRecord record = new ConsumptionRecord();
            record.setStudentId(studentId);
            record.setConsumptionNo("CNS" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + UUID.randomUUID().toString().substring(0, 6));
            record.setAmount(loginFee);
            record.setType(Constants.CONSUMPTION_TYPE_LOGIN);
            record.setDescription("登录扣费（" + electricityUsage + "度电）");
            record.setBalanceBefore(account.getBalance());
            record.setBalanceAfter(account.getBalance().subtract(loginFee));
            consumptionRecordMapper.insert(record);

            dto.setSuccess(true);
            dto.setMessage("扣费成功");
            return Result.success(dto);
        } else {
            // 余额不足，创建待缴费记录（负余额表示欠费）
            BigDecimal shortfall = loginFee.subtract(account.getBalance());

            // 扣除剩余余额（如果有）
            if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
                userAccountMapper.deductBalance(studentId, account.getBalance());
            }

            // 获取宿舍信息用于remark字段
            String dormitoryInfo = "";
            if (dormitoryId != null) {
                Dormitory dormitory = dormitoryMapper.selectById(dormitoryId);
                if (dormitory != null) {
                    dormitoryInfo = dormitory.getBuildingId() + "栋-" + dormitory.getDormitoryNumber();
                }
            }

            // 创建用电记录作为待缴费账单（使用登录扣费类型）
            ElectricityUsage usage = new ElectricityUsage();
            usage.setStudentId(studentId);
            usage.setFloorId(0L); // 登录扣费没有楼层，设为0
            usage.setDormitoryId(dormitoryId); // 关联宿舍
            usage.setUsageAmount(new BigDecimal(electricityUsage)); // 实际用电度数
            usage.setAmount(loginFee); // 应缴金额
            usage.setBalance(shortfall.negate()); // 负余额表示欠费金额
            usage.setRecordTime(LocalDateTime.now());
            usage.setStatus(Constants.ELECTRICITY_STATUS_UNPAID);
            electricityUsageMapper.insert(usage);

            // 创建缴费单（状态为待支付）
            String billNo = "BILL" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + usage.getId();
            PaymentBill bill = new PaymentBill();
            bill.setBillNo(billNo);
            bill.setStudentId(studentId);
            bill.setUsageId(usage.getId());
            bill.setAmount(loginFee);
            bill.setStatus(Constants.PAYMENT_STATUS_PENDING); // 待支付状态
            bill.setPaymentMethod("账户余额自动扣费（余额不足）");
            bill.setRemark(dormitoryInfo); // 记录宿舍信息
            paymentBillMapper.insert(bill);

            // 创建消费记录（标记为欠费）
            ConsumptionRecord record = new ConsumptionRecord();
            record.setStudentId(studentId);
            record.setConsumptionNo("CNS" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + UUID.randomUUID().toString().substring(0, 6));
            record.setAmount(loginFee);
            record.setType(Constants.CONSUMPTION_TYPE_LOGIN);
            record.setDescription("登录扣费（" + electricityUsage + "度电，余额不足，已生成待缴费记录）");
            record.setBalanceBefore(account.getBalance());
            record.setBalanceAfter(BigDecimal.ZERO);
            consumptionRecordMapper.insert(record);

            dto.setSuccess(false);
            dto.setMessage("余额不足");
            return Result.success(dto);
        }
    }

    /**
     * 获取当前电价
     */
    private BigDecimal getElectricityPrice() {
        SystemConfig config = systemConfigMapper.selectByKey("electricity_price");
        if (config != null && config.getConfigValue() != null) {
            try {
                return new BigDecimal(config.getConfigValue());
            } catch (NumberFormatException e) {
                return new BigDecimal("0.60"); // 默认电价
            }
        }
        return new BigDecimal("0.60"); // 默认电价
    }
}
