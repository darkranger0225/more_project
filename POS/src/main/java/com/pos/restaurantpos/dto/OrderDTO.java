package com.pos.restaurantpos.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {
    private Long tableId;
    private Integer personCount;
    private String remark;
    private Long memberId;
    private Integer pointsUsed;
    private List<OrderItemDTO> items;
}
