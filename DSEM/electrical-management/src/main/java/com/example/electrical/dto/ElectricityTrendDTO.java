package com.example.electrical.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ElectricityTrendDTO {
    
    // 统计类型：day-逐日, hour-逐时段, month-逐月
    private String type;
    
    // X轴标签
    private List<String> labels;
    
    // 用电量数据
    private List<BigDecimal> usageData;
    
    // 费用数据
    private List<BigDecimal> costData;
    
    // 统计摘要
    private BigDecimal totalUsage;
    private BigDecimal totalCost;
    private BigDecimal averageUsage;
    private BigDecimal maxUsage;
    private BigDecimal minUsage;
}
