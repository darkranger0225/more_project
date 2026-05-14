package com.example.electrical.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("electricity_usage")
public class ElectricityUsage extends BaseEntity {

    private Long studentId;

    private Long floorId;

    private Long dormitoryId;

    private BigDecimal usageAmount;

    private BigDecimal amount;

    private BigDecimal balance;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recordTime;

    private Integer status;
}
