package com.school.communication.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class StudentDTO {
    private Long id;
    
    @NotBlank(message = "学生姓名不能为空")
    private String studentName;
    
    private String studentNo;
    
    @NotNull(message = "班级不能为空")
    private Long classId;
    
    private String className;
    
    @NotNull(message = "家长不能为空")
    private Long parentId;
    
    private String parentName;
    
    private Integer gender;
    
    private LocalDate birthDate;
}