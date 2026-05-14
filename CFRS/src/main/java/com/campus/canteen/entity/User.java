package com.campus.canteen.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String studentId;
    
    private String username;
    
    private String password;
    
    private String phone;
    
    private String email;
    
    private String avatar;
    
    private Integer role;
    
    private Integer status;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
    
    public static final int ROLE_STUDENT = 0;
    public static final int ROLE_ADMIN = 1;
    
    public static final int STATUS_ENABLED = 1;
    public static final int STATUS_DISABLED = 0;
}
