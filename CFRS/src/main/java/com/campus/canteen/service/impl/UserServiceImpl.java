package com.campus.canteen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.canteen.entity.User;
import com.campus.canteen.mapper.UserMapper;
import com.campus.canteen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Override
    public User login(String username, String password) {
        User user = baseMapper.selectByUsername(username);
        if (user == null) {
            return null;
        }
        if (user.getStatus() == User.STATUS_DISABLED) {
            return null;
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }
        return user;
    }
    
    @Override
    public boolean register(User user) {
        if (baseMapper.selectByStudentId(user.getStudentId()) != null) {
            return false;
        }
        if (baseMapper.selectByUsername(user.getUsername()) != null) {
            return false;
        }
        if (user.getPhone() != null && !user.getPhone().trim().isEmpty() && baseMapper.selectByPhone(user.getPhone()) != null) {
            return false;
        }
        if (user.getEmail() != null && !user.getEmail().trim().isEmpty() && baseMapper.selectByEmail(user.getEmail()) != null) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(User.ROLE_STUDENT);
        user.setStatus(User.STATUS_ENABLED);
        return save(user);
    }
    
    @Override
    public User getByStudentId(String studentId) {
        return baseMapper.selectByStudentId(studentId);
    }
    
    @Override
    public User getByUsername(String username) {
        return baseMapper.selectByUsername(username);
    }
    
    @Override
    public User getByPhone(String phone) {
        return baseMapper.selectByPhone(phone);
    }
    
    @Override
    public User getByEmail(String email) {
        return baseMapper.selectByEmail(email);
    }
    
    @Override
    public boolean updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = getById(userId);
        if (user == null) {
            return false;
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        return updateById(user);
    }
    
    @Override
    public boolean resetPassword(String studentId, String phone, String newPassword) {
        User user = baseMapper.selectByStudentId(studentId);
        if (user == null) {
            return false;
        }
        if (!phone.equals(user.getPhone())) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        return updateById(user);
    }

    @Override
    public boolean resetPasswordByUsername(String username, String phone, String newPassword) {
        User user = baseMapper.selectByUsername(username);
        if (user == null) {
            return false;
        }
        if (!phone.equals(user.getPhone())) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        return updateById(user);
    }
}
