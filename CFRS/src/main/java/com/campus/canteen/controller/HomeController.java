package com.campus.canteen.controller;

import com.campus.canteen.entity.Dish;
import com.campus.canteen.entity.Notification;
import com.campus.canteen.entity.User;
import com.campus.canteen.entity.Window;
import com.campus.canteen.service.DishService;
import com.campus.canteen.service.NotificationService;
import com.campus.canteen.service.WindowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class HomeController {
    
    @Autowired
    private DishService dishService;
    
    @Autowired
    private WindowService windowService;
    
    @Autowired
    private NotificationService notificationService;
    
    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        
        List<Dish> topDishes = dishService.getTopRated(6);
        model.addAttribute("topDishes", topDishes);
        
        List<Window> openWindows = windowService.getOpenWindows();
        model.addAttribute("openWindows", openWindows);
        
        List<Notification> notifications = notificationService.getAllNotifications();
        if (notifications.size() > 5) {
            notifications = notifications.subList(0, 5);
        }
        model.addAttribute("notifications", notifications);
        
        return "index";
    }
    
    @GetMapping("/admin")
    public String adminPage(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getRole() != User.ROLE_ADMIN) {
            return "redirect:/";
        }
        return "admin/dashboard";
    }
}
