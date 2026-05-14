package com.example.electrical.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("building")
public class Building extends BaseEntity {

    private String buildingNumber;

    private String buildingName;

    private String description;
}
