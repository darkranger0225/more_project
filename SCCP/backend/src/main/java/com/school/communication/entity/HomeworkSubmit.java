package com.school.communication.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("homework_submit")
public class HomeworkSubmit {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long homeworkId;
    
    private Long studentId;
    
    private String content;
    
    private String attachmentUrl;
    
    private LocalDateTime submitTime;
    
    private BigDecimal score;
    
    private String comment;
    
    private Integer status;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}