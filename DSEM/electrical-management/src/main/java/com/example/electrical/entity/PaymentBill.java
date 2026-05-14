package com.example.electrical.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("payment_bill")
public class PaymentBill extends BaseEntity {
    
    private String billNo;
    
    private Long studentId;
    
    private Long usageId;
    
    private BigDecimal amount;
    
    private Integer status;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentTime;
    
    private String paymentMethod;
    
    private String remark;
}
