package com.pos.restaurantpos.controller;

import com.pos.restaurantpos.entity.*;
import com.pos.restaurantpos.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cashier")
@RequiredArgsConstructor
public class CashierController {

    private final TableService tableService;
    private final CategoryService categoryService;
    private final DishService dishService;
    private final OrderService orderService;
    private final UserService userService;
    private final StatisticsService statisticsService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("freeTables", tableService.countFree());
        model.addAttribute("occupiedTables", tableService.countOccupied());
        model.addAttribute("todayStats", statisticsService.getTodayStatistics());
        model.addAttribute("pendingOrders", orderService.findPendingOrders());
        return "cashier/dashboard";
    }

    @GetMapping("/tables")
    public String tables(Model model) {
        model.addAttribute("tables", tableService.findAll());
        return "cashier/tables";
    }

    @GetMapping("/order")
    public String orderPage(@RequestParam(required = false) Long tableId, Model model) {
        model.addAttribute("categories", categoryService.findEnabled());
        model.addAttribute("dishes", dishService.findOnSale());
        model.addAttribute("tables", tableService.findAll());
        if (tableId != null) {
            model.addAttribute("selectedTable", tableService.findById(tableId));
        }
        return "cashier/order";
    }

    @GetMapping("/orders")
    public String orders(@RequestParam(required = false) String orderNo,
                         @RequestParam(required = false) String date,
                         Model model) {
        model.addAttribute("orders", orderService.searchOrders(orderNo, date));
        return "cashier/orders";
    }

    @GetMapping("/order/detail/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        Order order = orderService.findById(id);
        model.addAttribute("order", order);
        return "cashier/order-detail";
    }

    @GetMapping("/checkout/{orderId}")
    public String checkoutPage(@PathVariable Long orderId, Model model) {
        Order order = orderService.findById(orderId);
        model.addAttribute("order", order);
        return "cashier/checkout";
    }

    @GetMapping("/table/order/{tableId}")
    public String getTableOrder(@PathVariable Long tableId) {
        List<Order> orders = orderService.findByTable(tableId);
        if (orders != null && !orders.isEmpty()) {
            Order latestOrder = orders.get(0);
            if (latestOrder.getStatus() != Order.Status.PAID && latestOrder.getStatus() != Order.Status.CANCELLED) {
                return "redirect:/cashier/order/detail/" + latestOrder.getId();
            }
        }
        return "redirect:/cashier/tables";
    }
}
