package com.school.communication.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ScoreDTO {
    private Long id;
    
    @NotNull(message = "学生不能为空")
    private Long studentId;
    
    private String studentName;
    
    @NotBlank(message = "考试名称不能为空")
    private String examName;
    
    @NotBlank(message = "科目不能为空")
    private String subject;
    
    @NotNull(message = "分数不能为空")
    private BigDecimal score;
    
    private BigDecimal fullScore;
    
    private Long teacherId;
    
    private String teacherName;
    
    @NotNull(message = "考试日期不能为空")
    private LocalDate examDate;
    
    private LocalDateTime createTime;
}
