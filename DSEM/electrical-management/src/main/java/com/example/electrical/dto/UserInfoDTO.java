package com.example.electrical.dto;

import lombok.Data;

@Data
public class UserInfoDTO {

    private Long id;
    private String account;
    private String name;
    private Integer role;
    private Integer gender;
    private Integer age;
    private String phone;
    private String photo;
    private Long floorId;
    private String floorNumber;
    private Long dormitoryId;
    private String dormitoryNumber;
    private String buildingInfo;
    private Integer status;
}
