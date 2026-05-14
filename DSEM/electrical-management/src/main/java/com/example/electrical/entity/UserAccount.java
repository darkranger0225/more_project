package com.example.electrical.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_account")
public class UserAccount extends BaseEntity {

    private Long studentId;

    private BigDecimal balance;

    private BigDecimal totalRecharge;

    private BigDecimal totalConsumption;
}
