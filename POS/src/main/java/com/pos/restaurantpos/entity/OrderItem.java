package com.pos.restaurantpos.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@javax.persistence.Table(name = "order_item")
public class OrderItem extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "order_id", insertable = false, updatable = false)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    @Column(name = "dish_id", insertable = false, updatable = false)
    private Long dishId;

    @Column(nullable = false, length = 100)
    private String dishName;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal dishPrice;

    @Column(nullable = false)
    private Integer quantity;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;
}
