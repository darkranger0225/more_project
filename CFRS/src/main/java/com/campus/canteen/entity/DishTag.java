package com.campus.canteen.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("dish_tag")
public class DishTag {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long dishId;
    private Long tagId;
}