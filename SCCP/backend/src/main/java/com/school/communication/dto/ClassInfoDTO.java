package com.school.communication.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ClassInfoDTO {
    private Long id;
    
    @NotBlank(message = "班级名称不能为空")
    private String className;
    
    @NotBlank(message = "年级不能为空")
    private String grade;
    
    private String description;
    
    private Long headTeacherId;
    
    private String headTeacherName;
}