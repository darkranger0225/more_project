package com.campus.canteen.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("review")
public class Review {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private Long dishId;
    
    private Double rating;
    
    private String content;
    
    private String image;
    
    private Integer isAnonymous;
    
    private Integer likeCount;
    
    private Integer status;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
    
    @TableField(exist = false)
    private String username;
    
    @TableField(exist = false)
    private String userAvatar;
    
    @TableField(exist = false)
    private String dishName;
    
    public static final int ANONYMOUS_YES = 1;
    public static final int ANONYMOUS_NO = 0;
    
    public static final int STATUS_PENDING = 0;
    public static final int STATUS_APPROVED = 1;
    public static final int STATUS_REJECTED = 2;
}
