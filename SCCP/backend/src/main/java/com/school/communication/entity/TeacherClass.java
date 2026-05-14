package com.school.communication.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("teacher_class")
public class TeacherClass {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long teacherId;
    
    private Long classId;
    
    private String subject;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}