package com.school.communication.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class HomeworkSubmitDTO {
    private Long id;
    
    private Long homeworkId;
    
    private Long studentId;
    
    private String studentName;
    
    private String content;
    
    private String attachmentUrl;
    
    private LocalDateTime submitTime;
    
    private BigDecimal score;
    
    private String comment;
    
    private Integer status;
}