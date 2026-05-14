package com.campus.canteen.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("dish")
public class Dish {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    
    private Long windowId;
    
    private String category;
    
    private String description;
    
    private BigDecimal price;
    
    private String image;
    
    private Double averageRating;
    
    private Integer reviewCount;
    
    private Integer status;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
    
    public static final int STATUS_ON_SALE = 1;
    public static final int STATUS_OFF_SALE = 0;
    
    public static final String CATEGORY_BREAKFAST = "早餐";
    public static final String CATEGORY_LUNCH = "午餐";
    public static final String CATEGORY_DINNER = "晚餐";
}
