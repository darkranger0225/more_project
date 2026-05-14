package com.pos.restaurantpos.controller;

import com.pos.restaurantpos.entity.*;
import com.pos.restaurantpos.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final CategoryService categoryService;
    private final DishService dishService;
    private final TableService tableService;
    private final OrderService orderService;
    private final StatisticsService statisticsService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("todayStats", statisticsService.getTodayStatistics());
        model.addAttribute("weekStats", statisticsService.getWeekStatistics());
        model.addAttribute("monthStats", statisticsService.getMonthStatistics());
        model.addAttribute("dailyStats", statisticsService.getDailyStatistics(7));
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @GetMapping("/users/add")
    public String addUserPage(Model model) {
        model.addAttribute("user", new User());
        return "admin/user-form";
    }

    @GetMapping("/users/edit/{id}")
    public String editUserPage(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "admin/user-form";
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "admin/categories";
    }

    @GetMapping("/categories/add")
    public String addCategoryPage(Model model) {
        model.addAttribute("category", new Category());
        return "admin/category-form";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategoryPage(@PathVariable Long id, Model model) {
        model.addAttribute("category", categoryService.findById(id));
        return "admin/category-form";
    }

    @GetMapping("/dishes")
    public String dishes(Model model) {
        model.addAttribute("dishes", dishService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "admin/dishes";
    }

    @GetMapping("/dishes/add")
    public String addDishPage(Model model) {
        model.addAttribute("dish", new Dish());
        model.addAttribute("categories", categoryService.findEnabled());
        return "admin/dish-form";
    }

    @GetMapping("/dishes/edit/{id}")
    public String editDishPage(@PathVariable Long id, Model model) {
        model.addAttribute("dish", dishService.findById(id));
        model.addAttribute("categories", categoryService.findAll());
        return "admin/dish-form";
    }

    @GetMapping("/tables")
    public String tables(Model model) {
        model.addAttribute("tables", tableService.findAll());
        return "admin/tables";
    }

    @GetMapping("/tables/add")
    public String addTablePage(Model model) {
        model.addAttribute("table", new Table());
        return "admin/table-form";
    }

    @GetMapping("/tables/edit/{id}")
    public String editTablePage(@PathVariable Long id, Model model) {
        model.addAttribute("table", tableService.findById(id));
        return "admin/table-form";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "admin/orders";
    }

    @GetMapping("/statistics")
    public String statistics(Model model) {
        model.addAttribute("todayStats", statisticsService.getTodayStatistics());
        model.addAttribute("weekStats", statisticsService.getWeekStatistics());
        model.addAttribute("monthStats", statisticsService.getMonthStatistics());
        model.addAttribute("dailyStats", statisticsService.getDailyStatistics(30));
        return "admin/statistics";
    }
}
