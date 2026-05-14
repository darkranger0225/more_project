package com.example.electrical.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoginDeductionDTO {
    
    private Boolean success;
    
    private BigDecimal amount;
    
    private BigDecimal electricityUsage;
    
    private String message;
}
