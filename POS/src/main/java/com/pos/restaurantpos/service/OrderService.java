package com.pos.restaurantpos.service;

import com.pos.restaurantpos.dto.OrderDTO;
import com.pos.restaurantpos.dto.OrderItemDTO;
import com.pos.restaurantpos.entity.*;
import com.pos.restaurantpos.repository.DishRepository;
import com.pos.restaurantpos.repository.OrderItemRepository;
import com.pos.restaurantpos.repository.OrderRepository;
import com.pos.restaurantpos.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final TableRepository tableRepository;
    private final DishRepository dishRepository;
    private final MemberService memberService;

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
    }

    public Order findByOrderNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public List<Order> findByTable(Long tableId) {
        return orderRepository.findByTableIdAndStatusNotOrderByCreateTimeDesc(tableId, Order.Status.CANCELLED);
    }

    public List<Order> findPendingOrders() {
        return orderRepository.findByStatusOrderByCreateTimeDesc(Order.Status.PENDING);
    }

    public List<Order> findTodayOrders() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        return orderRepository.findByCreateTimeBetweenOrderByCreateTimeDesc(startOfDay, endOfDay);
    }

    public List<Order> searchOrders(String orderNo, String date) {
        if (orderNo != null && !orderNo.trim().isEmpty()) {
            return orderRepository.findByOrderNoContainingOrderByCreateTimeDesc(orderNo.trim());
        }
        if (date != null && !date.trim().isEmpty()) {
            LocalDateTime searchDate = java.time.LocalDate.parse(date).atStartOfDay();
            LocalDateTime startOfDay = searchDate.withHour(0).withMinute(0).withSecond(0);
            LocalDateTime endOfDay = searchDate.withHour(23).withMinute(59).withSecond(59);
            return orderRepository.findByDate(startOfDay, endOfDay);
        }
        return findTodayOrders();
    }

    @Transactional
    public Order createOrder(OrderDTO orderDTO, User cashier) {
        Table table = tableRepository.findById(orderDTO.getTableId())
                .orElseThrow(() -> new RuntimeException("桌台不存在"));

        if (table.getStatus() == Table.Status.OCCUPIED) {
            throw new RuntimeException("该桌台已被占用");
        }

        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setTable(table);
        order.setPersonCount(orderDTO.getPersonCount());
        order.setStatus(Order.Status.PENDING);
        order.setPayMethod(Order.PayMethod.CASH);
        order.setCashier(cashier);
        order.setRemark(orderDTO.getRemark());
        order.setTotalAmount(BigDecimal.ZERO);
        order.setPointsUsed(0);
        order.setPointsDiscount(BigDecimal.ZERO);

        if (orderDTO.getMemberId() != null) {
            Member member = memberService.findById(orderDTO.getMemberId());
            order.setMember(member);
            
            if (orderDTO.getPointsUsed() != null && orderDTO.getPointsUsed() > 0) {
                if (member.getPoints() < orderDTO.getPointsUsed()) {
                    throw new RuntimeException("会员积分不足");
                }
                order.setPointsUsed(orderDTO.getPointsUsed());
                BigDecimal discount = new BigDecimal(orderDTO.getPointsUsed()).divide(new BigDecimal("100"));
                order.setPointsDiscount(discount);
            }
        }

        Order savedOrder = orderRepository.save(order);

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemDTO itemDTO : orderDTO.getItems()) {
            Dish dish = dishRepository.findById(itemDTO.getDishId())
                    .orElseThrow(() -> new RuntimeException("菜品不存在"));

            OrderItem item = new OrderItem();
            item.setOrder(savedOrder);
            item.setDish(dish);
            item.setDishName(dish.getName());
            item.setDishPrice(dish.getPrice());
            item.setQuantity(itemDTO.getQuantity());
            item.setSubtotal(dish.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));

            orderItemRepository.save(item);
            totalAmount = totalAmount.add(item.getSubtotal());
        }

        BigDecimal finalAmount = totalAmount.subtract(order.getPointsDiscount());
        if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
            finalAmount = BigDecimal.ZERO;
        }
        savedOrder.setTotalAmount(finalAmount);
        orderRepository.save(savedOrder);

        table.setStatus(Table.Status.OCCUPIED);
        tableRepository.save(table);

        return savedOrder;
    }

    @Transactional
    public void startCooking(Long orderId) {
        Order order = findById(orderId);
        if (order.getStatus() != Order.Status.PENDING) {
            throw new RuntimeException("订单状态不正确");
        }
        order.setStatus(Order.Status.COOKING);
        orderRepository.save(order);
    }

    @Transactional
    public void serveOrder(Long orderId) {
        Order order = findById(orderId);
        if (order.getStatus() != Order.Status.COOKING) {
            throw new RuntimeException("订单状态不正确");
        }
        order.setStatus(Order.Status.SERVED);
        orderRepository.save(order);
    }

    @Transactional
    public void payOrder(Long orderId, Order.PayMethod payMethod) {
        Order order = findById(orderId);
        if (order.getStatus() == Order.Status.PAID || order.getStatus() == Order.Status.CANCELLED) {
            throw new RuntimeException("订单状态不正确");
        }

        order.setStatus(Order.Status.PAID);
        order.setPayMethod(payMethod);
        order.setPayTime(LocalDateTime.now());
        orderRepository.save(order);

        if (order.getMember() != null) {
            if (order.getPointsUsed() > 0) {
                memberService.usePoints(order.getMember().getId(), order.getPointsUsed());
            }
            BigDecimal originalAmount = order.getTotalAmount().add(order.getPointsDiscount());
            memberService.addPoints(order.getMember().getId(), originalAmount);
        }

        Table table = order.getTable();
        table.setStatus(Table.Status.FREE);
        tableRepository.save(table);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = findById(orderId);
        if (order.getStatus() == Order.Status.PAID) {
            throw new RuntimeException("已支付订单不能取消");
        }

        order.setStatus(Order.Status.CANCELLED);
        orderRepository.save(order);

        Table table = order.getTable();
        table.setStatus(Table.Status.FREE);
        tableRepository.save(table);
    }

    private String generateOrderNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomStr = String.format("%04d", (int) (Math.random() * 10000));
        return "O" + dateStr + randomStr;
    }
}
