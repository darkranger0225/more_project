package com.campus.canteen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.canteen.entity.Notification;

import java.util.List;

public interface NotificationService extends IService<Notification> {
    List<Notification> getAllNotifications();
    boolean addNotification(Long userId, Integer type, String content);
    List<Notification> getNotificationsByUserId(Long userId);
}