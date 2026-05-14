package com.pos.restaurantpos.controller;

import com.pos.restaurantpos.dto.OrderDTO;
import com.pos.restaurantpos.dto.PayDTO;
import com.pos.restaurantpos.entity.*;
import com.pos.restaurantpos.service.*;
import com.pos.restaurantpos.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final UserService userService;
    private final CategoryService categoryService;
    private final DishService dishService;
    private final TableService tableService;
    private final OrderService orderService;
    private final MemberService memberService;

    @GetMapping("/categories")
    public ResultVO<List<Category>> getCategories() {
        return ResultVO.success(categoryService.findEnabled());
    }

    @GetMapping("/dishes")
    public ResultVO<List<Dish>> getDishes(@RequestParam(required = false) Long categoryId) {
        if (categoryId != null) {
            return ResultVO.success(dishService.findByCategory(categoryId));
        }
        return ResultVO.success(dishService.findOnSale());
    }

    @GetMapping("/tables")
    public ResultVO<List<Table>> getTables() {
        return ResultVO.success(tableService.findAll());
    }

    @GetMapping("/tables/free")
    public ResultVO<List<Table>> getFreeTables() {
        return ResultVO.success(tableService.findFree());
    }

    @PostMapping("/tables/{id}/occupy")
    public ResultVO<Void> occupyTable(@PathVariable Long id) {
        tableService.occupy(id);
        return ResultVO.success();
    }

    @PostMapping("/tables/{id}/free")
    public ResultVO<Void> freeTable(@PathVariable Long id) {
        tableService.free(id);
        return ResultVO.success();
    }

    @PostMapping("/orders")
    public ResultVO<Order> createOrder(@RequestBody OrderDTO orderDTO, Authentication authentication) {
        User cashier = userService.findByUsername(authentication.getName());
        Order order = orderService.createOrder(orderDTO, cashier);
        return ResultVO.success(order);
    }

    @GetMapping("/orders/{id}")
    public ResultVO<Order> getOrder(@PathVariable Long id) {
        return ResultVO.success(orderService.findById(id));
    }

    @GetMapping("/orders/today")
    public ResultVO<List<Order>> getTodayOrders() {
        return ResultVO.success(orderService.findTodayOrders());
    }

    @GetMapping("/orders/table/{tableId}")
    public ResultVO<List<Order>> getOrdersByTable(@PathVariable Long tableId) {
        return ResultVO.success(orderService.findByTable(tableId));
    }

    @PostMapping("/orders/{id}/cooking")
    public ResultVO<Void> startCooking(@PathVariable Long id) {
        orderService.startCooking(id);
        return ResultVO.success();
    }

    @PostMapping("/orders/{id}/serve")
    public ResultVO<Void> serveOrder(@PathVariable Long id) {
        orderService.serveOrder(id);
        return ResultVO.success();
    }

    @PostMapping("/orders/{id}/pay")
    public ResultVO<Void> payOrder(@PathVariable Long id, @RequestBody PayDTO payDTO) {
        orderService.payOrder(id, payDTO.getPayMethod());
        return ResultVO.success();
    }

    @PostMapping("/orders/{id}/cancel")
    public ResultVO<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResultVO.success();
    }

    @GetMapping("/members")
    public ResultVO<List<Member>> getMembers() {
        return ResultVO.success(memberService.findEnabled());
    }

    @GetMapping("/members/{id}")
    public ResultVO<Member> getMember(@PathVariable Long id) {
        return ResultVO.success(memberService.findById(id));
    }

    @GetMapping("/members/phone/{phone}")
    public ResultVO<Member> getMemberByPhone(@PathVariable String phone) {
        return ResultVO.success(memberService.findByPhone(phone));
    }

    @PostMapping("/members")
    public ResultVO<Member> createMember(@RequestBody Member member) {
        return ResultVO.success(memberService.createMember(member));
    }

    @PutMapping("/members/{id}")
    public ResultVO<Member> updateMember(@PathVariable Long id, @RequestBody Member member) {
        return ResultVO.success(memberService.updateMember(id, member));
    }

    @PostMapping("/members/{id}/enable")
    public ResultVO<Void> enableMember(@PathVariable Long id) {
        memberService.enableMember(id);
        return ResultVO.success();
    }

    @PostMapping("/members/{id}/disable")
    public ResultVO<Void> disableMember(@PathVariable Long id) {
        memberService.disableMember(id);
        return ResultVO.success();
    }

    @DeleteMapping("/members/{id}")
    public ResultVO<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResultVO.success();
    }
}
