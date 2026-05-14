package com.school.communication.controller;

import com.school.communication.dto.Result;
import com.school.communication.dto.UserDTO;
import com.school.communication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/info")
    public Result<UserDTO> getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        UserDTO user = userService.getUserById(userId);
        return Result.success(user);
    }
    
    @GetMapping("/list")
    public Result<List<UserDTO>> getUserList(@RequestParam(required = false) String role) {
        List<UserDTO> list = userService.getUserList(role);
        return Result.success(list);
    }
    
    @GetMapping("/{id}")
    public Result<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return Result.success(user);
    }
    
    @PostMapping
    public Result<Void> createUser(@Validated @RequestBody UserDTO userDTO) {
        try {
            userService.register(userDTO);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PutMapping
    public Result<Void> updateUser(@Validated @RequestBody UserDTO userDTO) {
        try {
            userService.updateUser(userDTO);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }
    
    @PostMapping("/change-password")
    public Result<Void> changePassword(@RequestBody ChangePasswordRequest request, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        try {
            userService.changePassword(userId, request.getOldPassword(), request.getNewPassword());
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
    
    public static class ChangePasswordRequest {
        private String oldPassword;
        private String newPassword;
        
        public String getOldPassword() { return oldPassword; }
        public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
}