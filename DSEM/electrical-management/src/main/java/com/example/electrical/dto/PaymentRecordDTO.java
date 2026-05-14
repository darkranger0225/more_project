package com.example.electrical.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRecordDTO {
    
    private Long id;
    
    private String billNo;
    
    private BigDecimal amount;
    
    private String paymentTime;
    
    private String paymentMethod;
    
    private Integer status;
    
    private String dormitoryInfo; // 宿舍信息（从remark字段获取）
}
