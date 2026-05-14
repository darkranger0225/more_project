package com.school.communication.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("score")
public class Score {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long studentId;
    
    private String examName;
    
    private String subject;
    
    private BigDecimal score;
    
    private BigDecimal fullScore;
    
    private Long teacherId;
    
    private LocalDate examDate;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    @TableField(select = false)
    private Integer deleted;
}
