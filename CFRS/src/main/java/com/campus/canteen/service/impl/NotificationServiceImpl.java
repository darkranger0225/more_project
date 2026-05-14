package com.campus.canteen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.canteen.entity.Notification;
import com.campus.canteen.entity.User;
import com.campus.canteen.mapper.NotificationMapper;
import com.campus.canteen.service.NotificationService;
import com.campus.canteen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    @Autowired
    private UserService userService;

    @Override
    public List<Notification> getAllNotifications() {
        List<Notification> notifications = list(new QueryWrapper<Notification>()
                .eq("deleted", 0)
                .orderByDesc("create_time"));
        
        for (Notification notification : notifications) {
            if (notification.getUserId() != null) {
                User user = userService.getById(notification.getUserId());
                if (user != null) {
                    notification.setUsername(user.getUsername());
                }
            }
        }
        return notifications;
    }

    @Override
    public boolean addNotification(Long userId, Integer type, String content) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setContent(content);
        return save(notification);
    }

    @Override
    public List<Notification> getNotificationsByUserId(Long userId) {
        List<Notification> notifications = list(new QueryWrapper<Notification>()
                .eq("user_id", userId)
                .eq("deleted", 0)
                .orderByDesc("create_time"));
        
        for (Notification notification : notifications) {
            if (notification.getUserId() != null) {
                User user = userService.getById(notification.getUserId());
                if (user != null) {
                    notification.setUsername(user.getUsername());
                }
            }
        }
        return notifications;
    }
}