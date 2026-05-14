package com.example.electrical.controller;

import com.example.electrical.common.Result;
import com.example.electrical.entity.ConsumptionRecord;
import com.example.electrical.entity.RechargeRecord;
import com.example.electrical.entity.UserAccount;
import com.example.electrical.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/info")
    public Result<UserAccount> getAccountInfo(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error("请先登录");
        }
        return accountService.getAccountInfo(userId);
    }

    @PostMapping("/recharge/create")
    public Result<String> createRechargeOrder(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error("请先登录");
        }
        
        Object amountObj = params.get("amount");
        if (amountObj == null) {
            return Result.error("请输入充值金额");
        }
        
        BigDecimal amount = new BigDecimal(amountObj.toString());
        return accountService.createRechargeOrder(userId, amount);
    }

    @PostMapping("/recharge/confirm")
    public Result<Void> confirmRecharge(@RequestBody Map<String, Object> params) {
        Object rechargeNoObj = params.get("rechargeNo");
        if (rechargeNoObj == null) {
            return Result.error("充值单号不能为空");
        }
        String rechargeNo = rechargeNoObj.toString();
        return accountService.confirmRecharge(rechargeNo);
    }

    @GetMapping("/recharge/records")
    public Result<List<RechargeRecord>> getRechargeRecords(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error("请先登录");
        }
        return accountService.getRechargeRecords(userId);
    }

    @GetMapping("/consumption/records")
    public Result<List<ConsumptionRecord>> getConsumptionRecords(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error("请先登录");
        }
        return accountService.getConsumptionRecords(userId);
    }

    private Long getUserIdFromRequest(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        if (userId == null) {
            return null;
        }
        return (Long) userId;
    }
}
