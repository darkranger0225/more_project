package com.campus.canteen.controller;

import com.campus.canteen.common.Result;
import com.campus.canteen.entity.User;
import com.campus.canteen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserAdminController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/list")
    public Result<List<User>> getUserList() {
        List<User> users = userService.list();
        return Result.success(users);
    }
}
