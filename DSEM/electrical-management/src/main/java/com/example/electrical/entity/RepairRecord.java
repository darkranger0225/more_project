package com.example.electrical.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("repair_record")
public class RepairRecord extends BaseEntity {
    
    private String title;
    
    private String content;
    
    private String image;
    
    private Long studentId;
    
    private Integer status;
    
    private String reply;
    
    private Long handlerId;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime handleTime;
}
