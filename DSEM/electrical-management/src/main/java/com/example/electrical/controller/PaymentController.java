package com.example.electrical.controller;

import com.example.electrical.common.Result;
import com.example.electrical.entity.PaymentBill;
import com.example.electrical.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/bill/list")
    public Result<List<PaymentBill>> getBillList(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error("请先登录");
        }
        return paymentService.getBillList(userId);
    }

    @GetMapping("/bill/{id}")
    public Result<PaymentBill> getBillDetail(@PathVariable Long id) {
        return paymentService.getBillDetail(id);
    }

    @PostMapping("/pay")
    public Result<Void> payBill(@RequestParam Long usageId, HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error("请先登录");
        }
        return paymentService.payBill(userId, usageId);
    }

    @GetMapping("/bill/all")
    public Result<List<PaymentBill>> getAllBills() {
        return paymentService.getAllBills();
    }

    private Long getUserIdFromRequest(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        if (userId == null) {
            return null;
        }
        return (Long) userId;
    }
}
