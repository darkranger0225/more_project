package com.campus.canteen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.canteen.entity.User;

public interface UserService extends IService<User> {
    
    User login(String username, String password);
    
    boolean register(User user);
    
    User getByStudentId(String studentId);
    
    User getByUsername(String username);
    
    User getByPhone(String phone);
    
    User getByEmail(String email);
    
    boolean updatePassword(Long userId, String oldPassword, String newPassword);

    boolean resetPassword(String studentId, String phone, String newPassword);

    boolean resetPasswordByUsername(String username, String phone, String newPassword);
}
