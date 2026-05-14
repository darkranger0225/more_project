package com.example.electrical.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("announcement")
public class Announcement extends BaseEntity {
    
    private String title;
    
    private String content;
    
    private String image;
    
    private Long publisherId;
    
    private Integer status;
}
