package com.campus.canteen.controller;

import com.campus.canteen.common.Result;
import com.campus.canteen.entity.Dish;
import com.campus.canteen.entity.Favorite;
import com.campus.canteen.entity.User;
import com.campus.canteen.entity.Window;
import com.campus.canteen.service.DishService;
import com.campus.canteen.service.FavoriteService;
import com.campus.canteen.service.UserService;
import com.campus.canteen.service.WindowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private FavoriteService favoriteService;
    
    @Autowired
    private DishService dishService;
    
    @Autowired
    private WindowService windowService;
    
    @GetMapping("/profile")
    public String profilePage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        // 获取用户收藏
        List<Favorite> favorites = favoriteService.getFavoritesByUserId(user.getId());
        List<Dish> favoriteDishes = new ArrayList<>();
        List<Window> favoriteWindows = new ArrayList<>();
        
        for (Favorite favorite : favorites) {
            if (favorite.getTargetType() == 1) { // 1-菜品
                Dish dish = dishService.getById(favorite.getTargetId());
                if (dish != null) {
                    favoriteDishes.add(dish);
                }
            } else if (favorite.getTargetType() == 2) { // 2-窗口
                Window window = windowService.getById(favorite.getTargetId());
                if (window != null) {
                    favoriteWindows.add(window);
                }
            }
        }
        
        model.addAttribute("user", user);
        model.addAttribute("favoriteDishes", favoriteDishes);
        model.addAttribute("favoriteWindows", favoriteWindows);
        return "profile";
    }
    
    @GetMapping("/password")
    public String passwordPage(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        return "password";
    }
    
    @GetMapping("/api/user/info")
    @ResponseBody
    public Result<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "未登录");
        }
        return Result.success(user);
    }
    
    @PutMapping("/api/user/info")
    @ResponseBody
    public Result<String> updateUserInfo(@RequestBody User userInfo, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "未登录");
        }
        
        // 手机号校验
        if (userInfo.getPhone() != null && !userInfo.getPhone().trim().isEmpty()) {
            String phoneRegex = "^1[3-9]\\d{9}$";
            if (!userInfo.getPhone().matches(phoneRegex)) {
                return Result.error("请输入有效的11位手机号码");
            }
            // 手机号唯一性校验
            User existingPhoneUser = userService.getByPhone(userInfo.getPhone());
            if (existingPhoneUser != null && !existingPhoneUser.getId().equals(user.getId())) {
                return Result.error("该手机号已被使用");
            }
        }
        
        // 邮箱校验
        if (userInfo.getEmail() != null && !userInfo.getEmail().trim().isEmpty()) {
            String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
            if (!userInfo.getEmail().matches(emailRegex)) {
                return Result.error("请输入有效的邮箱地址");
            }
            // 邮箱唯一性校验
            User existingEmailUser = userService.getByEmail(userInfo.getEmail());
            if (existingEmailUser != null && !existingEmailUser.getId().equals(user.getId())) {
                return Result.error("该邮箱已被使用");
            }
        }
        
        User existingUser = userService.getById(user.getId());
        existingUser.setPhone(userInfo.getPhone());
        existingUser.setEmail(userInfo.getEmail());
        
        boolean success = userService.updateById(existingUser);
        if (success) {
            session.setAttribute("user", existingUser);
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }
    
    @PostMapping("/api/user/password")
    @ResponseBody
    public Result<String> updatePassword(@RequestParam String oldPassword,
                                          @RequestParam String newPassword,
                                          HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "未登录");
        }
        
        if (newPassword == null || newPassword.length() < 6) {
            return Result.error("新密码长度不能少于6位");
        }
        
        boolean success = userService.updatePassword(user.getId(), oldPassword, newPassword);
        if (success) {
            return Result.success("密码修改成功");
        }
        return Result.error("原密码错误");
    }
    
    @PostMapping("/api/user/reset-password")
    @ResponseBody
    public Result<String> resetPassword(@RequestParam String username,
                                         @RequestParam String phone,
                                         @RequestParam String newPassword) {
        if (newPassword == null || newPassword.length() < 6) {
            return Result.error("新密码长度不能少于6位");
        }

        boolean success = userService.resetPasswordByUsername(username, phone, newPassword);
        if (success) {
            return Result.success("密码重置成功");
        }
        return Result.error("用户名或手机号错误");
    }
    
    @PostMapping("/api/user/avatar")
    @ResponseBody
    public Result<String> uploadAvatar(@RequestParam("avatar") MultipartFile avatar, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "未登录");
        }
        
        if (avatar == null || avatar.isEmpty()) {
            return Result.error("请选择要上传的头像");
        }
        
        String avatarPath = saveAvatar(avatar);
        if (avatarPath != null) {
            User existingUser = userService.getById(user.getId());
            existingUser.setAvatar(avatarPath);
            boolean success = userService.updateById(existingUser);
            if (success) {
                session.setAttribute("user", existingUser);
                return Result.success("头像上传成功");
            }
        }
        return Result.error("头像上传失败");
    }
    
    private String saveAvatar(MultipartFile avatar) {
        try {
            // 获取项目根目录的绝对路径
            String projectPath = System.getProperty("user.dir");
            String uploadDir = projectPath + "/src/main/resources/static/images/avatar/";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            String fileName = System.currentTimeMillis() + "_" + Objects.requireNonNull(avatar.getOriginalFilename());
            String filePath = uploadDir + fileName;
            File dest = new File(filePath);
            avatar.transferTo(dest);
            
            // 返回相对路径，用于数据库存储和前端访问
            return "/images/avatar/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
