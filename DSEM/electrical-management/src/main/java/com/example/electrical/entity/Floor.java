package com.example.electrical.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("floor_info")
public class Floor extends BaseEntity {

    private Long buildingId;

    private String floorNumber;

    private String description;
}
