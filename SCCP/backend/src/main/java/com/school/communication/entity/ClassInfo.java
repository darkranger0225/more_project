package com.school.communication.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("class")
public class ClassInfo {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String className;
    
    private String grade;
    
    private String description;
    
    private Long headTeacherId;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    @TableField(select = false)
    private Integer deleted;
}