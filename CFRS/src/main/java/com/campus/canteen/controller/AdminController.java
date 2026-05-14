package com.campus.canteen.controller;

import com.campus.canteen.common.Result;
import com.campus.canteen.entity.User;
import com.campus.canteen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/users")
    public Result<List<User>> getAllUsers() {
        List<User> users = userService.list();
        return Result.success(users);
    }
    
    @PostMapping("/users/{id}/status")
    public Result<String> updateUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        user.setStatus(status);
        boolean success = userService.updateById(user);
        if (success) {
            return Result.success("状态更新成功");
        }
        return Result.error("状态更新失败");
    }
}
