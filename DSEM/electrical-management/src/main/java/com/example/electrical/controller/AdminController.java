package com.example.electrical.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.electrical.common.Constants;
import com.example.electrical.common.Result;
import com.example.electrical.dto.DormitoryElectricityDTO;
import com.example.electrical.dto.PaymentRecordDTO;
import com.example.electrical.dto.RepairHandleDTO;
import com.example.electrical.dto.StudentDTO;
import com.example.electrical.entity.Dormitory;
import com.example.electrical.entity.ElectricityUsage;
import com.example.electrical.entity.Floor;
import com.example.electrical.entity.PaymentBill;
import com.example.electrical.entity.RepairRecord;
import com.example.electrical.entity.SystemConfig;
import com.example.electrical.entity.User;
import com.example.electrical.entity.UserAccount;
import com.example.electrical.mapper.DormitoryMapper;
import com.example.electrical.mapper.ElectricityUsageMapper;
import com.example.electrical.mapper.FloorMapper;
import com.example.electrical.mapper.PaymentBillMapper;
import com.example.electrical.mapper.SystemConfigMapper;
import com.example.electrical.mapper.UserAccountMapper;
import com.example.electrical.mapper.UserMapper;
import com.example.electrical.service.ElectricityService;
import com.example.electrical.service.RepairService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserMapper userMapper;
    private final FloorMapper floorMapper;
    private final DormitoryMapper dormitoryMapper;
    private final SystemConfigMapper systemConfigMapper;
    private final UserAccountMapper userAccountMapper;
    private final ElectricityUsageMapper electricityUsageMapper;
    private final PaymentBillMapper paymentBillMapper;
    private final ElectricityService electricityService;
    private final RepairService repairService;
    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/student/list")
    public Result<Page<StudentDTO>> getStudentList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<User> pageParam = new Page<>(page, size);
        Page<User> userPage = userMapper.selectPage(pageParam,
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                .eq(User::getRole, Constants.ROLE_STUDENT)
                .eq(User::getDeleted, 0));
        
        // 转换为StudentDTO并填充宿舍信息
        List<StudentDTO> studentDTOList = new ArrayList<>();
        for (User user : userPage.getRecords()) {
            StudentDTO dto = convertToStudentDTO(user);
            studentDTOList.add(dto);
        }
        
        Page<StudentDTO> dtoPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        dtoPage.setRecords(studentDTOList);
        return Result.success(dtoPage);
    }

    @GetMapping("/student/search")
    public Result<List<StudentDTO>> searchStudents(@RequestParam String keyword) {
        // 根据账号或姓名搜索学生
        List<User> userList = userMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                .eq(User::getRole, Constants.ROLE_STUDENT)
                .eq(User::getDeleted, 0)
                .and(wrapper -> wrapper
                    .like(User::getAccount, keyword)
                    .or()
                    .like(User::getName, keyword)));
        
        // 转换为StudentDTO并填充宿舍信息
        List<StudentDTO> studentDTOList = new ArrayList<>();
        for (User user : userList) {
            StudentDTO dto = convertToStudentDTO(user);
            studentDTOList.add(dto);
        }
        
        return Result.success(studentDTOList);
    }

    private StudentDTO convertToStudentDTO(User user) {
        StudentDTO dto = new StudentDTO();
        dto.setId(user.getId());
        dto.setAccount(user.getAccount());
        dto.setName(user.getName());
        dto.setPhone(user.getPhone());
        dto.setGender(user.getGender());
        dto.setAge(user.getAge());
        dto.setStatus(user.getStatus());
        dto.setFloorId(user.getFloorId());
        dto.setDormitoryId(user.getDormitoryId());
        dto.setCreateTime(user.getCreateTime());
        
        // 查询宿舍信息
        if (user.getDormitoryId() != null) {
            Dormitory dormitory = dormitoryMapper.selectById(user.getDormitoryId());
            if (dormitory != null) {
                dto.setDormitoryNumber(dormitory.getDormitoryNumber());
                // 查询楼层信息
                if (dormitory.getBuildingId() != null) {
                    dto.setFloorNumber(dormitory.getBuildingId() + "栋");
                }
            }
        }
        
        return dto;
    }

    @PostMapping("/student/add")
    public Result<Void> addStudent(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Constants.ROLE_STUDENT);
        user.setStatus(Constants.USER_STATUS_ENABLED);
        userMapper.insert(user);
        return Result.success();
    }

    @PutMapping("/student/update")
    public Result<Void> updateStudent(@RequestBody User user) {
        // 验证手机号格式
        if (user.getPhone() != null && !user.getPhone().isEmpty()) {
            if (!user.getPhone().matches("^1[3-9]\\d{9}$")) {
                return Result.error("手机号格式不正确");
            }
        }

        // 验证密码
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            if (user.getPassword().length() < 6 || user.getPassword().length() > 20) {
                return Result.error("密码长度应为6-20位");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            // 不更新密码字段
            user.setPassword(null);
        }
        userMapper.updateById(user);
        return Result.success();
    }

    @DeleteMapping("/student/{id}")
    public Result<Void> deleteStudent(@PathVariable Long id) {
        userMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/student/{id}")
    public Result<User> getStudentDetail(@PathVariable Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.error("学生不存在");
        }
        return Result.success(user);
    }

    @GetMapping("/admin/list")
    public Result<List<User>> getAdminList() {
        List<User> list = userMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                .eq(User::getRole, Constants.ROLE_ADMIN)
                .eq(User::getDeleted, 0));
        return Result.success(list);
    }

    @PostMapping("/admin/add")
    public Result<Void> addAdmin(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Constants.ROLE_ADMIN);
        user.setStatus(Constants.USER_STATUS_ENABLED);
        userMapper.insert(user);
        return Result.success();
    }

    @PutMapping("/password/reset/{id}")
    public Result<Void> resetPassword(@PathVariable Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        user.setPassword(passwordEncoder.encode("123456"));
        userMapper.updateById(user);
        return Result.success();
    }

    @GetMapping("/floor/list")
    public Result<List<Floor>> getFloorList() {
        List<Floor> list = floorMapper.selectList(null);
        return Result.success(list);
    }

    @PostMapping("/floor/add")
    public Result<Void> addFloor(@RequestBody Floor floor) {
        floorMapper.insert(floor);
        return Result.success();
    }

    @GetMapping("/config/list")
    public Result<List<SystemConfig>> getConfigList() {
        List<SystemConfig> list = systemConfigMapper.selectList(null);
        return Result.success(list);
    }

    @PutMapping("/config/update")
    public Result<Void> updateConfig(@RequestBody SystemConfig config) {
        // 根据 configKey 查找现有配置
        SystemConfig existingConfig = systemConfigMapper.selectByKey(config.getConfigKey());
        if (existingConfig != null) {
            // 更新现有配置
            existingConfig.setConfigValue(config.getConfigValue());
            if (config.getDescription() != null) {
                existingConfig.setDescription(config.getDescription());
            }
            systemConfigMapper.updateById(existingConfig);
        } else {
            // 创建新配置
            systemConfigMapper.insert(config);
        }
        return Result.success();
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = new HashMap<>();

        // 学生总数
        Long studentCount = userMapper.selectCount(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                .eq(User::getRole, Constants.ROLE_STUDENT)
                .eq(User::getDeleted, 0));
        stats.put("studentCount", studentCount);

        // 待处理报修数
        var pendingRepairs = repairService.getPendingRepairs();
        stats.put("pendingRepairCount", pendingRepairs.getData() != null ? pendingRepairs.getData().size() : 0);

        // 楼层数
        Long floorCount = floorMapper.selectCount(null);
        stats.put("floorCount", floorCount);

        return Result.success(stats);
    }

    /**
     * 获取宿舍用电信息（按楼号和寝室号查询）
     */
    @GetMapping("/electricity/usage")
    public Result<List<DormitoryElectricityDTO>> getDormitoryElectricityUsage(
            @RequestParam Long buildingId,
            @RequestParam(required = false) String dormitoryNumber) {
        
        // 获取该楼栋的所有宿舍
        List<Dormitory> dormitories = dormitoryMapper.selectByBuildingId(buildingId);
        
        // 如果指定了寝室号，进行筛选
        if (dormitoryNumber != null && !dormitoryNumber.trim().isEmpty()) {
            dormitories = dormitories.stream()
                .filter(d -> d.getDormitoryNumber() != null && 
                    d.getDormitoryNumber().contains(dormitoryNumber.trim()))
                .collect(Collectors.toList());
        }
        
        List<DormitoryElectricityDTO> result = new ArrayList<>();
        
        for (Dormitory dormitory : dormitories) {
            DormitoryElectricityDTO dto = new DormitoryElectricityDTO();
            dto.setId(dormitory.getId());
            dto.setDormitoryId(dormitory.getId());
            dto.setDormitoryNumber(dormitory.getDormitoryNumber());
            dto.setBuildingId(buildingId);
            dto.setBuildingName(buildingId + "栋");
            
            // 获取该宿舍的学生信息
            List<User> students = userMapper.selectByDormitoryId(dormitory.getId());
            if (!students.isEmpty()) {
                User student = students.get(0);
                dto.setStudentId(student.getId());
                dto.setStudentName(student.getName());
                
                // 获取学生账户信息
                UserAccount account = userAccountMapper.selectByStudentId(student.getId());
                if (account != null) {
                    dto.setBalance(account.getBalance());
                } else {
                    dto.setBalance(BigDecimal.ZERO);
                }
            } else {
                dto.setBalance(BigDecimal.ZERO);
            }
            
            // 获取该宿舍的用电记录
            List<ElectricityUsage> usages = electricityUsageMapper.selectByDormitoryId(dormitory.getId());
            
            // 计算累计用电量和费用
            BigDecimal totalUsage = BigDecimal.ZERO;
            BigDecimal totalCost = BigDecimal.ZERO;
            BigDecimal unpaidAmount = BigDecimal.ZERO;
            int unpaidCount = 0;
            
            for (ElectricityUsage usage : usages) {
                if (usage.getUsageAmount() != null) {
                    totalUsage = totalUsage.add(usage.getUsageAmount());
                }
                if (usage.getAmount() != null) {
                    totalCost = totalCost.add(usage.getAmount());
                }
                // 统计未缴费金额
                if (usage.getStatus() != null && usage.getStatus() == Constants.ELECTRICITY_STATUS_UNPAID) {
                    unpaidCount++;
                    if (usage.getBalance() != null) {
                        unpaidAmount = unpaidAmount.add(usage.getBalance().abs());
                    }
                }
            }
            
            dto.setTotalUsage(totalUsage);
            dto.setTotalCost(totalCost);
            dto.setUnpaidAmount(unpaidAmount);
            
            // 设置状态：有欠费则为欠费状态
            if (unpaidCount > 0 || (dto.getBalance() != null && dto.getBalance().compareTo(BigDecimal.ZERO) < 0)) {
                dto.setStatus(Constants.ELECTRICITY_STATUS_UNPAID);
            } else {
                dto.setStatus(Constants.ELECTRICITY_STATUS_PAID);
            }
            
            // 获取缴费记录
            List<PaymentBill> paymentBills = new ArrayList<>();
            
            // 方式1：通过学生ID查询
            if (dto.getStudentId() != null) {
                paymentBills.addAll(paymentBillMapper.selectByStudentId(dto.getStudentId()));
            }
            
            // 方式2：通过宿舍信息（remark字段）查询 - 用于查询历史记录或没有学生关联的记录
            String dormitoryInfo = buildingId + "栋-" + dormitory.getDormitoryNumber();
            List<PaymentBill> billsByDormitory = paymentBillMapper.selectByRemarkLike(dormitoryInfo);
            paymentBills.addAll(billsByDormitory);
            
            // 去重并按时间排序
            List<PaymentRecordDTO> paymentRecords = paymentBills.stream()
                .distinct()
                .sorted((b1, b2) -> b2.getCreateTime().compareTo(b1.getCreateTime()))
                .map(bill -> {
                    PaymentRecordDTO record = new PaymentRecordDTO();
                    record.setId(bill.getId());
                    record.setBillNo(bill.getBillNo());
                    record.setAmount(bill.getAmount());
                    record.setPaymentTime(bill.getPaymentTime() != null ? 
                        bill.getPaymentTime().toString() : "");
                    record.setPaymentMethod(bill.getPaymentMethod());
                    record.setStatus(bill.getStatus());
                    record.setDormitoryInfo(bill.getRemark()); // 从remark字段获取宿舍信息
                    return record;
                })
                .limit(5) // 只显示最近5条缴费记录
                .collect(Collectors.toList());
            dto.setPaymentRecords(paymentRecords);
            
            result.add(dto);
        }
        
        return Result.success(result);
    }

    /**
     * 获取所有报修列表（管理员）
     */
    @GetMapping("/repair/list")
    public Result<List<Map<String, Object>>> getAllRepairs() {
        List<RepairRecord> repairs = repairService.getAllRepairs().getData();
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (RepairRecord repair : repairs) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", repair.getId());
            map.put("title", repair.getTitle());
            map.put("content", repair.getContent());
            map.put("image", repair.getImage());
            map.put("status", repair.getStatus());
            map.put("reply", repair.getReply());
            map.put("createTime", repair.getCreateTime());
            map.put("handleTime", repair.getHandleTime());
            
            // 获取学生信息
            User student = userMapper.selectById(repair.getStudentId());
            if (student != null) {
                map.put("studentName", student.getName());
                map.put("studentId", student.getId());
            } else {
                map.put("studentName", "未知");
                map.put("studentId", repair.getStudentId());
            }
            
            // 获取处理人信息
            if (repair.getHandlerId() != null) {
                User handler = userMapper.selectById(repair.getHandlerId());
                if (handler != null) {
                    map.put("handlerName", handler.getName());
                } else {
                    map.put("handlerName", "管理员");
                }
            } else {
                map.put("handlerName", null);
            }
            
            result.add(map);
        }
        
        return Result.success(result);
    }

    /**
     * 处理报修
     */
    @PostMapping("/repair/handle")
    public Result<Void> handleRepair(@RequestBody RepairHandleDTO handleDTO, HttpServletRequest request) {
        Long adminId = getUserIdFromRequest(request);
        if (adminId == null) {
            return Result.error("请先登录");
        }
        return repairService.handleRepair(adminId, handleDTO);
    }

    private Long getUserIdFromRequest(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        if (userId == null) {
            return null;
        }
        return (Long) userId;
    }
}
