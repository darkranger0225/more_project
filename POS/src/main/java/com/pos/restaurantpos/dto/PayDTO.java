package com.pos.restaurantpos.dto;

import com.pos.restaurantpos.entity.Order;
import lombok.Data;

@Data
public class PayDTO {
    private Long orderId;
    private Order.PayMethod payMethod;
}
