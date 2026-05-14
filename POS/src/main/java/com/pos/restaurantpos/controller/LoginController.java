package com.pos.restaurantpos.controller;

import com.pos.restaurantpos.entity.User;
import com.pos.restaurantpos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(Model model, String error) {
        if (error != null) {
            model.addAttribute("error", "用户名或密码错误");
        }
        return "common/login";
    }

    @GetMapping("/redirect")
    public String redirect(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/cashier/dashboard";
    }

    @PostConstruct
    public void init() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRealName("系统管理员");
            admin.setPhone("13800138000");
            admin.setRole(User.Role.ADMIN);
            admin.setStatus(User.Status.ENABLED);
            userRepository.save(admin);
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "common/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String resetPassword(
            @RequestParam String username,
            @RequestParam String phone,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model) {
        
        // 检查两次密码是否一致
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "两次输入的密码不一致");
            return "common/forgot-password";
        }

        // 查找用户
        User user = userRepository.findByUsername(username)
                .orElse(null);

        if (user == null) {
            model.addAttribute("error", "用户名不存在");
            return "common/forgot-password";
        }

        // 验证手机号
        if (user.getPhone() == null || !user.getPhone().equals(phone)) {
            model.addAttribute("error", "手机号不正确");
            return "common/forgot-password";
        }

        // 重置密码
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        model.addAttribute("success", "密码重置成功，请使用新密码登录");
        return "common/forgot-password";
    }
}
