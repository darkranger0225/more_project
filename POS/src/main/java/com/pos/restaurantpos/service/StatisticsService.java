package com.pos.restaurantpos.service;

import com.pos.restaurantpos.repository.OrderItemRepository;
import com.pos.restaurantpos.repository.OrderRepository;
import com.pos.restaurantpos.vo.StatisticsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public StatisticsVO getTodayStatistics() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        return getStatisticsByTimeRange(startOfDay, endOfDay);
    }

    public StatisticsVO getWeekStatistics() {
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        LocalDateTime startOfWeek = endOfDay.minus(7, ChronoUnit.DAYS).withHour(0).withMinute(0).withSecond(0);
        return getStatisticsByTimeRange(startOfWeek, endOfDay);
    }

    public StatisticsVO getMonthStatistics() {
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        LocalDateTime startOfMonth = endOfDay.minus(30, ChronoUnit.DAYS).withHour(0).withMinute(0).withSecond(0);
        return getStatisticsByTimeRange(startOfMonth, endOfDay);
    }

    public StatisticsVO getStatisticsByTimeRange(LocalDateTime start, LocalDateTime end) {
        StatisticsVO vo = new StatisticsVO();

        BigDecimal totalAmount = orderRepository.sumTotalAmountByTimeRange(start, end);
        vo.setTotalAmount(totalAmount != null ? totalAmount : BigDecimal.ZERO);

        Long totalOrders = orderRepository.countPaidOrdersByTimeRange(start, end);
        vo.setTotalOrders(totalOrders != null ? totalOrders : 0L);

        if (totalOrders != null && totalOrders > 0) {
            vo.setAvgOrderAmount(totalAmount.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP));
        } else {
            vo.setAvgOrderAmount(BigDecimal.ZERO);
        }

        List<Object[]> popularDishes = orderItemRepository.findPopularDishes(start, end);
        List<Map<String, Object>> popularDishesList = new ArrayList<>();
        for (Object[] row : popularDishes) {
            Map<String, Object> dish = new HashMap<>();
            dish.put("dishId", row[0] != null ? row[0] : 0);
            dish.put("dishName", row[1] != null ? row[1].toString() : "未知菜品");
            dish.put("totalQuantity", row[2] != null ? row[2] : 0);
            dish.put("totalAmount", row[3] != null ? row[3] : BigDecimal.ZERO);
            popularDishesList.add(dish);
        }
        vo.setPopularDishes(popularDishesList);

        long totalDishes = 0L;
        for (Map<String, Object> d : popularDishesList) {
            Object qty = d.get("totalQuantity");
            if (qty instanceof Number) {
                totalDishes += ((Number) qty).longValue();
            } else if (qty != null) {
                totalDishes += Long.parseLong(qty.toString());
            }
        }
        vo.setTotalDishes(totalDishes);

        return vo;
    }

    public List<Map<String, Object>> getDailyStatistics(int days) {
        List<Map<String, Object>> result = new ArrayList<>();
        LocalDateTime end = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

        for (int i = days - 1; i >= 0; i--) {
            LocalDateTime dayStart = end.minus(i, ChronoUnit.DAYS).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime dayEnd = dayStart.withHour(23).withMinute(59).withSecond(59);

            BigDecimal amount = orderRepository.sumTotalAmountByTimeRange(dayStart, dayEnd);
            Long count = orderRepository.countPaidOrdersByTimeRange(dayStart, dayEnd);

            Map<String, Object> dayStat = new HashMap<>();
            dayStat.put("date", dayStart.toLocalDate().toString());
            dayStat.put("amount", amount != null ? amount : BigDecimal.ZERO);
            dayStat.put("count", count != null ? count : 0L);
            result.add(dayStat);
        }

        return result;
    }
}
