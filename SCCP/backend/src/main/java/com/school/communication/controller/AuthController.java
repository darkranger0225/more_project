package com.school.communication.controller;

import com.school.communication.dto.LoginDTO;
import com.school.communication.dto.LoginResponseDTO;
import com.school.communication.dto.Result;
import com.school.communication.dto.UserDTO;
import com.school.communication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/login")
    public Result<LoginResponseDTO> login(@Validated @RequestBody LoginDTO loginDTO) {
        try {
            LoginResponseDTO response = userService.login(loginDTO);
            return Result.success(response);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/register")
    public Result<Void> register(@Validated @RequestBody UserDTO userDTO) {
        try {
            userService.register(userDTO);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/verify-user")
    public Result<Map<String, Object>> verifyUser(@RequestBody Map<String, String> params) {
        try {
            String username = params.get("username");
            String phone = params.get("phone");
            
            Long userId = userService.verifyUser(username, phone);
            
            Map<String, Object> result = new HashMap<>();
            result.put("userId", userId);
            
            return Result.success(result);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/reset-password")
    public Result<Void> resetPassword(@RequestBody Map<String, String> params) {
        try {
            Long userId = Long.parseLong(params.get("userId"));
            String newPassword = params.get("newPassword");
            
            userService.resetPassword(userId, newPassword);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}