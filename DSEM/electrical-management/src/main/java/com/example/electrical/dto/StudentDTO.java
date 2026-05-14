package com.example.electrical.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudentDTO {
    private Long id;
    private String account;
    private String name;
    private String phone;
    private Integer gender;
    private Integer age;
    private Integer status;
    private Long floorId;
    private Long dormitoryId;
    private String floorNumber;
    private String dormitoryNumber;
    private LocalDateTime createTime;
}
