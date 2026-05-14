package com.campus.canteen.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("notification")
public class Notification {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId; // 发送通知的用户ID
    private Integer type; // 1-系统通知，2-活动通知
    private String content;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableLogic
    private Integer deleted;
    
    @TableField(exist = false)
    private String username; // 发布者用户名，不存储到数据库
}