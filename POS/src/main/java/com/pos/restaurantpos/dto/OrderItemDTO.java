package com.pos.restaurantpos.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private Long dishId;
    private String dishName;
    private BigDecimal dishPrice;
    private Integer quantity;
}
