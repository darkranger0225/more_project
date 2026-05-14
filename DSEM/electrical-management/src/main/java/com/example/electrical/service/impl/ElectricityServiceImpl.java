package com.example.electrical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.electrical.common.Constants;
import com.example.electrical.common.Result;
import com.example.electrical.dto.ElectricityUsageDTO;
import com.example.electrical.entity.ElectricityUsage;
import com.example.electrical.entity.SystemConfig;
import com.example.electrical.entity.User;
import com.example.electrical.mapper.ElectricityUsageMapper;
import com.example.electrical.mapper.SystemConfigMapper;
import com.example.electrical.mapper.UserMapper;
import com.example.electrical.service.ElectricityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ElectricityServiceImpl extends ServiceImpl<ElectricityUsageMapper, ElectricityUsage> implements ElectricityService {

    @Autowired
    private ElectricityUsageMapper electricityUsageMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Override
    public Result<List<ElectricityUsage>> getUsageList(Long studentId) {
        // 获取用户宿舍ID
        User user = userMapper.selectById(studentId);
        if (user == null || user.getDormitoryId() == null) {
            // 如果没有宿舍信息，只查询自己的记录
            List<ElectricityUsage> list = electricityUsageMapper.selectByStudentId(studentId);
            return Result.success(list);
        }
        // 查询同宿舍的所有用电记录
        List<ElectricityUsage> list = electricityUsageMapper.selectByDormitoryId(user.getDormitoryId());
        return Result.success(list);
    }

    @Override
    public Result<BigDecimal> getBalance(Long studentId) {
        // 获取用户宿舍ID
        User user = userMapper.selectById(studentId);
        if (user == null || user.getDormitoryId() == null) {
            // 如果没有宿舍信息，只查询自己的待缴费金额
            BigDecimal balance = electricityUsageMapper.selectUnpaidAmountByStudentId(studentId);
            return Result.success(balance != null ? balance : BigDecimal.ZERO);
        }
        // 查询同宿舍的所有待缴费金额
        List<ElectricityUsage> unpaidList = electricityUsageMapper.selectUnpaidByDormitoryId(user.getDormitoryId());
        BigDecimal totalBalance = BigDecimal.ZERO;
        for (ElectricityUsage usage : unpaidList) {
            if (usage.getBalance() != null && usage.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                // 负余额表示欠费，取绝对值累加
                totalBalance = totalBalance.add(usage.getBalance().abs());
            } else if (usage.getBalance() != null) {
                totalBalance = totalBalance.add(usage.getBalance());
            }
        }
        return Result.success(totalBalance);
    }

    @Override
    public Result<List<ElectricityUsage>> getUnpaidList(Long studentId) {
        // 获取用户宿舍ID
        User user = userMapper.selectById(studentId);
        if (user == null || user.getDormitoryId() == null) {
            // 如果没有宿舍信息，只查询自己的待缴费记录
            List<ElectricityUsage> list = electricityUsageMapper.selectUnpaidByStudentId(studentId);
            return Result.success(list);
        }
        // 查询同宿舍的所有待缴费记录
        List<ElectricityUsage> list = electricityUsageMapper.selectUnpaidByDormitoryId(user.getDormitoryId());
        return Result.success(list);
    }

    @Override
    public Result<Void> addUsage(Long adminId, ElectricityUsageDTO usageDTO) {
        // 检查学生是否存在
        User student = userMapper.selectById(usageDTO.getStudentId());
        if (student == null) {
            return Result.error("学生不存在");
        }

        if (student.getRole() != Constants.ROLE_STUDENT) {
            return Result.error("该用户不是学生");
        }

        // 获取电费单价
        SystemConfig priceConfig = systemConfigMapper.selectByKey("electricity_price");
        if (priceConfig == null) {
            return Result.error("系统配置错误：未设置电费单价");
        }
        BigDecimal price = new BigDecimal(priceConfig.getConfigValue());

        // 计算金额
        BigDecimal amount = usageDTO.getUsageAmount().multiply(price);

        ElectricityUsage usage = new ElectricityUsage();
        usage.setStudentId(usageDTO.getStudentId());
        usage.setFloorId(student.getFloorId());
        usage.setUsageAmount(usageDTO.getUsageAmount());
        usage.setAmount(amount);
        usage.setBalance(amount);
        usage.setRecordTime(LocalDateTime.parse(usageDTO.getRecordTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        usage.setStatus(Constants.ELECTRICITY_STATUS_UNPAID);

        electricityUsageMapper.insert(usage);
        return Result.success();
    }

    @Override
    public Result<List<ElectricityUsage>> getUsageByFloor(Long floorId) {
        List<ElectricityUsage> list = electricityUsageMapper.selectByFloorId(floorId);
        return Result.success(list);
    }
}
