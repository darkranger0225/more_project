package com.pos.restaurantpos.repository;

import com.pos.restaurantpos.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);

    @Query("SELECT oi.dishId, oi.dishName, SUM(oi.quantity) as totalQuantity, SUM(oi.subtotal) as totalAmount " +
           "FROM OrderItem oi JOIN oi.order o " +
           "WHERE o.status != 'CANCELLED' AND o.createTime BETWEEN :start AND :end " +
           "GROUP BY oi.dishId, oi.dishName " +
           "ORDER BY totalQuantity DESC")
    List<Object[]> findPopularDishes(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
