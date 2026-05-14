package com.example.electrical.controller;

import com.example.electrical.common.Result;
import com.example.electrical.dto.*;
import com.example.electrical.entity.SystemConfig;
import com.example.electrical.mapper.SystemConfigMapper;
import com.example.electrical.service.AccountService;
import com.example.electrical.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AccountService accountService;
    private final SystemConfigMapper systemConfigMapper;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginDTO loginDTO) {
        Result<Map<String, Object>> loginResult = userService.loginWithRole(loginDTO);
        if (loginResult.getCode() != 200) {
            return Result.error(loginResult.getMessage());
        }

        return loginResult;
    }

    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterDTO registerDTO) {
        return userService.register(registerDTO);
    }

    @GetMapping("/info")
    public Result<UserInfoDTO> getUserInfo(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error("请先登录");
        }
        return userService.getUserInfo(userId);
    }

    @PutMapping("/update")
    public Result<Void> updateUserInfo(@RequestBody UserInfoDTO userInfoDTO, HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error("请先登录");
        }
        return userService.updateUserInfo(userId, userInfoDTO);
    }

    @PutMapping("/password")
    public Result<Void> updatePassword(@Valid @RequestBody PasswordDTO passwordDTO, HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error("请先登录");
        }
        return userService.updatePassword(userId, passwordDTO);
    }

    @PostMapping("/reset-password")
    public Result<Void> resetPassword(@RequestBody Map<String, String> resetData) {
        String account = resetData.get("account");
        String phone = resetData.get("phone");
        String newPassword = resetData.get("newPassword");
        return userService.resetPassword(account, phone, newPassword);
    }

    @PostMapping("/login/deduct")
    public Result<Map<String, Object>> deductForLogin(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error("请先登录");
        }
        
        var deductResult = accountService.deductForLogin(userId);
        
        Map<String, Object> result = new HashMap<>();
        if (deductResult.getData() != null) {
            result.put("success", deductResult.getData().getSuccess());
            result.put("amount", deductResult.getData().getAmount().toString());
            result.put("electricityUsage", deductResult.getData().getElectricityUsage().toString());
            result.put("message", deductResult.getData().getMessage());
        } else {
            result.put("success", false);
            result.put("amount", "0.00");
            result.put("electricityUsage", "0");
            result.put("message", "扣费失败");
        }
        
        return Result.success(result);
    }
    
    /**
     * 获取当前电价
     */
    private BigDecimal getElectricityPrice() {
        SystemConfig config = systemConfigMapper.selectByKey("electricity_price");
        if (config != null && config.getConfigValue() != null) {
            try {
                return new BigDecimal(config.getConfigValue());
            } catch (NumberFormatException e) {
                return new BigDecimal("0.60"); // 默认电价
            }
        }
        return new BigDecimal("0.60"); // 默认电价
    }

    private Long getUserIdFromRequest(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        if (userId == null) {
            return null;
        }
        return (Long) userId;
    }
}
