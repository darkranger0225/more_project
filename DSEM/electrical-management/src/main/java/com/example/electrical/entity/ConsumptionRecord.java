package com.example.electrical.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("consumption_record")
public class ConsumptionRecord extends BaseEntity {

    private Long studentId;

    private String consumptionNo;

    private BigDecimal amount;

    private Integer type;

    private String description;

    private BigDecimal balanceBefore;

    private BigDecimal balanceAfter;
}
