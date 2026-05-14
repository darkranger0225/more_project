package com.pos.restaurantpos.controller;

import com.pos.restaurantpos.entity.*;
import com.pos.restaurantpos.service.*;
import com.pos.restaurantpos.vo.ResultVO;
import com.pos.restaurantpos.vo.StatisticsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminApiController {

    private final UserService userService;
    private final CategoryService categoryService;
    private final DishService dishService;
    private final TableService tableService;
    private final StatisticsService statisticsService;

    @PostMapping("/users")
    public ResultVO<User> createUser(@RequestBody User user) {
        return ResultVO.success(userService.createUser(user));
    }

    @PutMapping("/users/{id}")
    public ResultVO<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResultVO.success(userService.updateUser(id, user));
    }

    @DeleteMapping("/users/{id}")
    public ResultVO<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResultVO.success();
    }

    @PostMapping("/users/{id}/disable")
    public ResultVO<Void> disableUser(@PathVariable Long id) {
        userService.disableUser(id);
        return ResultVO.success();
    }

    @PostMapping("/users/{id}/enable")
    public ResultVO<Void> enableUser(@PathVariable Long id) {
        userService.enableUser(id);
        return ResultVO.success();
    }

    @PostMapping("/categories")
    public ResultVO<Category> createCategory(@RequestBody Category category) {
        return ResultVO.success(categoryService.create(category));
    }

    @PutMapping("/categories/{id}")
    public ResultVO<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return ResultVO.success(categoryService.update(id, category));
    }

    @DeleteMapping("/categories/{id}")
    public ResultVO<Void> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResultVO.success();
    }

    @PostMapping("/categories/{id}/enable")
    public ResultVO<Void> enableCategory(@PathVariable Long id) {
        categoryService.enable(id);
        return ResultVO.success();
    }

    @PostMapping("/categories/{id}/disable")
    public ResultVO<Void> disableCategory(@PathVariable Long id) {
        categoryService.disable(id);
        return ResultVO.success();
    }

    @PostMapping("/dishes")
    public ResultVO<Dish> createDish(@RequestBody Dish dish) {
        return ResultVO.success(dishService.create(dish));
    }

    @PutMapping("/dishes/{id}")
    public ResultVO<Dish> updateDish(@PathVariable Long id, @RequestBody Dish dish) {
        return ResultVO.success(dishService.update(id, dish));
    }

    @DeleteMapping("/dishes/{id}")
    public ResultVO<Void> deleteDish(@PathVariable Long id) {
        dishService.delete(id);
        return ResultVO.success();
    }

    @PostMapping("/dishes/{id}/on-sale")
    public ResultVO<Void> onSaleDish(@PathVariable Long id) {
        dishService.onSale(id);
        return ResultVO.success();
    }

    @PostMapping("/dishes/{id}/off-sale")
    public ResultVO<Void> offSaleDish(@PathVariable Long id) {
        dishService.offSale(id);
        return ResultVO.success();
    }

    @PostMapping("/tables")
    public ResultVO<Table> createTable(@RequestBody Table table) {
        return ResultVO.success(tableService.create(table));
    }

    @PutMapping("/tables/{id}")
    public ResultVO<Table> updateTable(@PathVariable Long id, @RequestBody Table table) {
        return ResultVO.success(tableService.update(id, table));
    }

    @DeleteMapping("/tables/{id}")
    public ResultVO<Void> deleteTable(@PathVariable Long id) {
        tableService.delete(id);
        return ResultVO.success();
    }

    @GetMapping("/statistics/today")
    public ResultVO<StatisticsVO> getTodayStatistics() {
        return ResultVO.success(statisticsService.getTodayStatistics());
    }

    @GetMapping("/statistics/week")
    public ResultVO<StatisticsVO> getWeekStatistics() {
        return ResultVO.success(statisticsService.getWeekStatistics());
    }

    @GetMapping("/statistics/month")
    public ResultVO<StatisticsVO> getMonthStatistics() {
        return ResultVO.success(statisticsService.getMonthStatistics());
    }

    @GetMapping("/statistics/daily")
    public ResultVO<List<Map<String, Object>>> getDailyStatistics(@RequestParam(defaultValue = "7") int days) {
        return ResultVO.success(statisticsService.getDailyStatistics(days));
    }
}
