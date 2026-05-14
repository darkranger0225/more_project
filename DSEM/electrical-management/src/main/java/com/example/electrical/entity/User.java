package com.example.electrical.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class User extends BaseEntity {

    private String account;

    private String password;

    private String name;

    private Integer role;

    private Integer gender;

    private Integer age;

    private String phone;

    private String photo;

    private Long floorId;

    private Long dormitoryId;

    private Integer status;
}
