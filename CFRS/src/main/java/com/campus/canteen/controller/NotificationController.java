package com.campus.canteen.controller;

import com.campus.canteen.common.Result;
import com.campus.canteen.entity.User;
import com.campus.canteen.service.NotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/notification")
public class NotificationController {

    @Resource
    private NotificationService notificationService;

    @GetMapping("/all")
    @ResponseBody
    public Result getAllNotifications() {
        return Result.success(notificationService.getAllNotifications());
    }

    @PostMapping("/add")
    @ResponseBody
    public Result addNotification(Integer type, String content, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error("请先登录");
        }
        Long userId = user.getId();
        boolean result = notificationService.addNotification(userId, type, content);
        return result ? Result.success("通知发布成功") : Result.error("通知发布失败");
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public Result deleteNotification(@PathVariable Long id) {
        boolean result = notificationService.removeById(id);
        return result ? Result.success("删除成功") : Result.error("删除失败");
    }

    @GetMapping("/my")
    @ResponseBody
    public Result getMyNotifications(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error("请先登录");
        }
        return Result.success(notificationService.getNotificationsByUserId(user.getId()));
    }
}