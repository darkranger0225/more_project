package com.pos.restaurantpos.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class StatisticsVO {
    private BigDecimal totalAmount;
    private Long totalOrders;
    private Long totalDishes;
    private BigDecimal avgOrderAmount;
    private List<Map<String, Object>> popularDishes;
    private List<Map<String, Object>> hourlyStats;
    private List<Map<String, Object>> dailyStats;
}
