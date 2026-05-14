package com.example.electrical.service.impl;

import com.example.electrical.common.Result;
import com.example.electrical.dto.ElectricityTrendDTO;
import com.example.electrical.entity.ElectricityUsage;
import com.example.electrical.entity.SystemConfig;
import com.example.electrical.entity.User;
import com.example.electrical.mapper.ElectricityUsageMapper;
import com.example.electrical.mapper.SystemConfigMapper;
import com.example.electrical.mapper.UserMapper;
import com.example.electrical.service.ElectricityTrendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ElectricityTrendServiceImpl implements ElectricityTrendService {

    private final ElectricityUsageMapper electricityUsageMapper;
    private final SystemConfigMapper systemConfigMapper;
    private final UserMapper userMapper;

    @Override
    public Result<ElectricityTrendDTO> getTrendData(Long studentId, String type) {
        ElectricityTrendDTO dto = new ElectricityTrendDTO();
        dto.setType(type);

        // 获取当前电价
        BigDecimal electricityPrice = getElectricityPrice();

        // 获取学生信息，得到宿舍ID
        User student = userMapper.selectById(studentId);
        Long dormitoryId = student != null ? student.getDormitoryId() : null;

        switch (type) {
            case "day":
                generateDayTrend(dto, studentId, dormitoryId, electricityPrice);
                break;
            case "hour":
                generateHourTrend(dto, studentId, dormitoryId, electricityPrice);
                break;
            case "month":
                generateMonthTrend(dto, studentId, dormitoryId, electricityPrice);
                break;
            default:
                generateDayTrend(dto, studentId, dormitoryId, electricityPrice);
        }

        return Result.success(dto);
    }

    /**
     * 生成逐日趋势数据（最近8天）
     * 优先使用真实数据，不足时用模拟数据填充
     */
    private void generateDayTrend(ElectricityTrendDTO dto, Long studentId, Long dormitoryId, BigDecimal price) {
        List<String> labels = new ArrayList<>();
        List<BigDecimal> usageData = new ArrayList<>();
        List<BigDecimal> costData = new ArrayList<>();

        LocalDate today = LocalDate.now();
        BigDecimal totalUsage = BigDecimal.ZERO;
        BigDecimal maxUsage = BigDecimal.ZERO;
        BigDecimal minUsage = new BigDecimal("9999");

        // 查询真实数据（如果有宿舍ID）
        Map<String, BigDecimal> realDataMap = new HashMap<>();
        if (dormitoryId != null) {
            LocalDate startDate = today.minusDays(7);
            List<ElectricityUsage> realRecords = electricityUsageMapper.selectByDormitoryId(dormitoryId);
            
            // 过滤最近8天的记录，并按日期聚合
            realRecords.stream()
                .filter(r -> r.getRecordTime() != null && 
                    r.getRecordTime().toLocalDate().isAfter(startDate.minusDays(1)))
                .forEach(r -> {
                    String dateKey = r.getRecordTime().toLocalDate().format(DateTimeFormatter.ofPattern("MM-dd"));
                    realDataMap.merge(dateKey, r.getUsageAmount() != null ? r.getUsageAmount() : BigDecimal.ZERO, BigDecimal::add);
                });
        }

        // 生成8天数据
        Random random = new Random(studentId);
        for (int i = 7; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            String dateKey = date.format(DateTimeFormatter.ofPattern("MM-dd"));
            labels.add(dateKey);

            BigDecimal usage;
            
            // 优先使用真实数据
            if (realDataMap.containsKey(dateKey) && realDataMap.get(dateKey).compareTo(BigDecimal.ZERO) > 0) {
                usage = realDataMap.get(dateKey);
            } else {
                // 使用模拟数据
                boolean isWeekend = date.getDayOfWeek().getValue() >= 6;
                BigDecimal baseUsage;
                if (isWeekend) {
                    baseUsage = new BigDecimal("15").add(new BigDecimal(random.nextInt(11)));
                } else {
                    baseUsage = new BigDecimal("8").add(new BigDecimal(random.nextInt(8)));
                }
                double fluctuation = 0.8 + (random.nextDouble() * 0.4);
                usage = baseUsage.multiply(new BigDecimal(fluctuation)).setScale(2, RoundingMode.HALF_UP);
            }

            usageData.add(usage);
            costData.add(usage.multiply(price).setScale(2, RoundingMode.HALF_UP));

            totalUsage = totalUsage.add(usage);
            if (usage.compareTo(maxUsage) > 0) maxUsage = usage;
            if (usage.compareTo(minUsage) < 0) minUsage = usage;
        }

        dto.setLabels(labels);
        dto.setUsageData(usageData);
        dto.setCostData(costData);
        dto.setTotalUsage(totalUsage);
        dto.setTotalCost(totalUsage.multiply(price).setScale(2, RoundingMode.HALF_UP));
        dto.setAverageUsage(totalUsage.divide(new BigDecimal("8"), 2, RoundingMode.HALF_UP));
        dto.setMaxUsage(maxUsage);
        dto.setMinUsage(minUsage.compareTo(new BigDecimal("9999")) == 0 ? BigDecimal.ZERO : minUsage);
    }

    /**
     * 生成逐时段趋势数据（8个时段）
     * 优先使用真实数据，不足时用模拟数据填充
     */
    private void generateHourTrend(ElectricityTrendDTO dto, Long studentId, Long dormitoryId, BigDecimal price) {
        List<String> labels = new ArrayList<>();
        List<BigDecimal> usageData = new ArrayList<>();
        List<BigDecimal> costData = new ArrayList<>();

        Random random = new Random(studentId);
        BigDecimal totalUsage = BigDecimal.ZERO;

        // 定义8个时段
        int[][] timeRanges = {
            {0, 3},    // 凌晨
            {3, 6},    // 清晨
            {6, 9},    // 早晨
            {9, 12},   // 上午
            {12, 15},  // 下午
            {15, 18},  // 傍晚
            {18, 21},  // 晚上
            {21, 24}   // 深夜
        };
        String[] timeLabels = {"凌晨", "清晨", "早晨", "上午", "下午", "傍晚", "晚上", "深夜"};

        // 查询真实数据（最近一天的时段分布）
        Map<Integer, BigDecimal> realDataMap = new HashMap<>();
        if (dormitoryId != null) {
            LocalDate yesterday = LocalDate.now().minusDays(1);
            List<ElectricityUsage> realRecords = electricityUsageMapper.selectByDormitoryId(dormitoryId);
            
            realRecords.stream()
                .filter(r -> r.getRecordTime() != null && 
                    r.getRecordTime().toLocalDate().equals(yesterday))
                .forEach(r -> {
                    int hour = r.getRecordTime().getHour();
                    realDataMap.merge(hour, r.getUsageAmount() != null ? r.getUsageAmount() : BigDecimal.ZERO, BigDecimal::add);
                });
        }

        for (int i = 0; i < 8; i++) {
            int startHour = timeRanges[i][0];
            int endHour = timeRanges[i][1];
            labels.add(timeLabels[i]);

            BigDecimal usage;
            
            // 尝试聚合该时段的真实数据
            BigDecimal realUsage = BigDecimal.ZERO;
            boolean hasRealData = false;
            for (int h = startHour; h < endHour; h++) {
                if (realDataMap.containsKey(h)) {
                    realUsage = realUsage.add(realDataMap.get(h));
                    hasRealData = true;
                }
            }
            
            if (hasRealData && realUsage.compareTo(BigDecimal.ZERO) > 0) {
                usage = realUsage;
            } else {
                // 使用模拟数据
                BigDecimal baseUsage;
                if (startHour >= 0 && startHour < 6) {
                    baseUsage = new BigDecimal("2").add(new BigDecimal(random.nextInt(21)).divide(new BigDecimal("10"), 2, RoundingMode.HALF_UP));
                } else if (startHour >= 6 && startHour < 9) {
                    baseUsage = new BigDecimal("3").add(new BigDecimal(random.nextInt(31)).divide(new BigDecimal("10"), 2, RoundingMode.HALF_UP));
                } else if (startHour >= 9 && startHour < 17) {
                    baseUsage = new BigDecimal("2").add(new BigDecimal(random.nextInt(21)).divide(new BigDecimal("10"), 2, RoundingMode.HALF_UP));
                } else if (startHour >= 17 && startHour < 21) {
                    baseUsage = new BigDecimal("6").add(new BigDecimal(random.nextInt(41)).divide(new BigDecimal("10"), 2, RoundingMode.HALF_UP));
                } else {
                    baseUsage = new BigDecimal("3").add(new BigDecimal(random.nextInt(31)).divide(new BigDecimal("10"), 2, RoundingMode.HALF_UP));
                }
                usage = baseUsage;
            }

            usageData.add(usage);
            costData.add(usage.multiply(price).setScale(2, RoundingMode.HALF_UP));
            totalUsage = totalUsage.add(usage);
        }

        dto.setLabels(labels);
        dto.setUsageData(usageData);
        dto.setCostData(costData);
        dto.setTotalUsage(totalUsage);
        dto.setTotalCost(totalUsage.multiply(price).setScale(2, RoundingMode.HALF_UP));
        dto.setAverageUsage(totalUsage.divide(new BigDecimal("8"), 2, RoundingMode.HALF_UP));
        dto.setMaxUsage(usageData.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
        dto.setMinUsage(usageData.stream().min(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
    }

    /**
     * 生成逐月趋势数据（最近6个月）
     * 优先使用真实数据，不足时用模拟数据填充
     */
    private void generateMonthTrend(ElectricityTrendDTO dto, Long studentId, Long dormitoryId, BigDecimal price) {
        List<String> labels = new ArrayList<>();
        List<BigDecimal> usageData = new ArrayList<>();
        List<BigDecimal> costData = new ArrayList<>();

        Random random = new Random(studentId);
        LocalDate today = LocalDate.now();
        BigDecimal totalUsage = BigDecimal.ZERO;
        BigDecimal maxUsage = BigDecimal.ZERO;
        BigDecimal minUsage = new BigDecimal("99999");

        // 查询真实数据
        Map<String, BigDecimal> realDataMap = new HashMap<>();
        if (dormitoryId != null) {
            LocalDate startDate = today.minusMonths(5).withDayOfMonth(1);
            List<ElectricityUsage> realRecords = electricityUsageMapper.selectByDormitoryId(dormitoryId);
            
            realRecords.stream()
                .filter(r -> r.getRecordTime() != null && 
                    r.getRecordTime().toLocalDate().isAfter(startDate.minusDays(1)))
                .forEach(r -> {
                    String monthKey = r.getRecordTime().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM"));
                    realDataMap.merge(monthKey, r.getUsageAmount() != null ? r.getUsageAmount() : BigDecimal.ZERO, BigDecimal::add);
                });
        }

        // 生成最近6个月的数据
        for (int i = 5; i >= 0; i--) {
            LocalDate date = today.minusMonths(i);
            String monthKey = date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            labels.add(monthKey);

            BigDecimal usage;
            
            // 优先使用真实数据
            if (realDataMap.containsKey(monthKey) && realDataMap.get(monthKey).compareTo(BigDecimal.ZERO) > 0) {
                usage = realDataMap.get(monthKey);
            } else {
                // 使用模拟数据
                int month = date.getMonthValue();
                BigDecimal baseUsage;
                if (month >= 6 && month <= 8) {
                    baseUsage = new BigDecimal("200").add(new BigDecimal(random.nextInt(101)));
                } else if (month >= 12 || month <= 2) {
                    baseUsage = new BigDecimal("150").add(new BigDecimal(random.nextInt(51)));
                } else {
                    baseUsage = new BigDecimal("100").add(new BigDecimal(random.nextInt(51)));
                }
                double fluctuation = 0.9 + (random.nextDouble() * 0.2);
                usage = baseUsage.multiply(new BigDecimal(fluctuation)).setScale(2, RoundingMode.HALF_UP);
            }

            usageData.add(usage);
            costData.add(usage.multiply(price).setScale(2, RoundingMode.HALF_UP));

            totalUsage = totalUsage.add(usage);
            if (usage.compareTo(maxUsage) > 0) maxUsage = usage;
            if (usage.compareTo(minUsage) < 0) minUsage = usage;
        }

        dto.setLabels(labels);
        dto.setUsageData(usageData);
        dto.setCostData(costData);
        dto.setTotalUsage(totalUsage);
        dto.setTotalCost(totalUsage.multiply(price).setScale(2, RoundingMode.HALF_UP));
        dto.setAverageUsage(totalUsage.divide(new BigDecimal("6"), 2, RoundingMode.HALF_UP));
        dto.setMaxUsage(maxUsage);
        dto.setMinUsage(minUsage.compareTo(new BigDecimal("99999")) == 0 ? BigDecimal.ZERO : minUsage);
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
                return new BigDecimal("0.60");
            }
        }
        return new BigDecimal("0.60");
    }
}
