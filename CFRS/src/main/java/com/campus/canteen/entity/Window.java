package com.campus.canteen.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("`window`")
public class Window {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    
    private Integer floor;
    
    private Integer windowNumber;
    
    private String description;
    
    private String image;
    
    private Integer status;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
    
    public static final int STATUS_OPEN = 1;
    public static final int STATUS_CLOSED = 0;
}
