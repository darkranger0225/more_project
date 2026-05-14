package com.pos.restaurantpos.repository;

import com.pos.restaurantpos.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNo(String orderNo);

    List<Order> findByTableIdAndStatusNotOrderByCreateTimeDesc(Long tableId, Order.Status status);

    List<Order> findByStatusOrderByCreateTimeDesc(Order.Status status);

    List<Order> findByCreateTimeBetweenOrderByCreateTimeDesc(LocalDateTime start, LocalDateTime end);

    @Query("SELECT o FROM Order o WHERE o.status != 'CANCELLED' AND o.createTime BETWEEN :start AND :end ORDER BY o.createTime DESC")
    List<Order> findValidOrdersByTimeRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT o FROM Order o WHERE o.createTime BETWEEN :start AND :end ORDER BY o.createTime DESC")
    List<Order> findByDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    List<Order> findByOrderNoContainingOrderByCreateTimeDesc(String orderNo);

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = 'PAID' AND o.createTime BETWEEN :start AND :end")
    BigDecimal sumTotalAmountByTimeRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'PAID' AND o.createTime BETWEEN :start AND :end")
    Long countPaidOrdersByTimeRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT o.table.id, COUNT(o) as orderCount FROM Order o WHERE o.status != 'CANCELLED' AND o.createTime BETWEEN :start AND :end GROUP BY o.table.id ORDER BY orderCount DESC")
    List<Object[]> findPopularTables(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
