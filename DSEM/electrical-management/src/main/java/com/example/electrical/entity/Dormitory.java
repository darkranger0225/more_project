package com.example.electrical.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dormitory")
public class Dormitory extends BaseEntity {

    private Long buildingId;

    private Long floorId;

    private String dormitoryNumber;

    private String description;
}
