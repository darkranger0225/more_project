package com.example.electrical.dto;

import com.example.electrical.entity.PaymentBill;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DormitoryElectricityDTO {
    
    private Long id;
    
    private Long dormitoryId;
    
    private String dormitoryNumber;
    
    private Long buildingId;
    
    private String buildingName;
    
    private Long studentId;
    
    private String studentName;
    
    private BigDecimal balance;
    
    private BigDecimal totalUsage;
    
    private BigDecimal totalCost;
    
    private BigDecimal unpaidAmount;
    
    private Integer status;
    
    private List<PaymentRecordDTO> paymentRecords;
}
