package com.campus.canteen.controller;

import com.campus.canteen.common.Result;
import com.campus.canteen.entity.User;
import com.campus.canteen.service.UserService;
import com.campus.canteen.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
    
    @PostMapping("/api/auth/login")
    @ResponseBody
    public Result<Map<String, Object>> login(@RequestParam String username, 
                                              @RequestParam String password,
                                              HttpSession session) {
        User user = userService.login(username, password);
        if (user == null) {
            return Result.error("用户名或密码错误");
        }
        
        String token = jwtUtils.generateToken(user);
        
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", user);
        
        session.setAttribute("user", user);
        
        return Result.success("登录成功", data);
    }
    
    @PostMapping("/api/auth/register")
    @ResponseBody
    public Result<String> register(@RequestBody User user) {
        if (user.getStudentId() == null || user.getStudentId().trim().isEmpty()) {
            return Result.error("学号不能为空");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            return Result.error("密码长度不能少于6位");
        }
        
        // 手机号校验
        if (user.getPhone() != null && !user.getPhone().trim().isEmpty()) {
            String phoneRegex = "^1[3-9]\\d{9}$";
            if (!user.getPhone().matches(phoneRegex)) {
                return Result.error("请输入有效的11位手机号码");
            }
        }
        
        // 邮箱校验
        if (user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
            String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
            if (!user.getEmail().matches(emailRegex)) {
                return Result.error("请输入有效的邮箱地址");
            }
        }
        
        // 唯一性校验
        if (userService.getByStudentId(user.getStudentId()) != null) {
            return Result.error("学号已存在");
        }
        if (userService.getByUsername(user.getUsername()) != null) {
            return Result.error("用户名已存在");
        }
        if (user.getPhone() != null && !user.getPhone().trim().isEmpty() && userService.getByPhone(user.getPhone()) != null) {
            return Result.error("手机号已被使用");
        }
        if (user.getEmail() != null && !user.getEmail().trim().isEmpty() && userService.getByEmail(user.getEmail()) != null) {
            return Result.error("邮箱已被使用");
        }
        
        boolean success = userService.register(user);
        if (!success) {
            return Result.error("注册失败，请重试");
        }
        return Result.success("注册成功");
    }
    
    @PostMapping("/api/auth/logout")
    @ResponseBody
    public Result<String> logout(HttpSession session) {
        session.invalidate();
        return Result.success("退出成功");
    }
    
    @GetMapping("/api/auth/check-student-id")
    @ResponseBody
    public Result<Boolean> checkStudentId(@RequestParam String studentId) {
        User user = userService.getByStudentId(studentId);
        return Result.success(user == null);
    }
    
    @GetMapping("/api/auth/check-username")
    @ResponseBody
    public Result<Boolean> checkUsername(@RequestParam String username) {
        User user = userService.getByUsername(username);
        return Result.success(user == null);
    }
}
