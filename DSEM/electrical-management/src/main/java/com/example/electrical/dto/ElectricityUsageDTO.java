package com.example.electrical.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ElectricityUsageDTO {
    
    @NotNull(message = "学生ID不能为空")
    private Long studentId;
    
    @NotNull(message = "用电量不能为空")
    private BigDecimal usageAmount;
    
    @NotNull(message = "记录时间不能为空")
    private String recordTime;
}
