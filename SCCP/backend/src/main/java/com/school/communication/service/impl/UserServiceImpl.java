package com.school.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school.communication.dto.LoginDTO;
import com.school.communication.dto.LoginResponseDTO;
import com.school.communication.dto.UserDTO;
import com.school.communication.entity.User;
import com.school.communication.mapper.UserMapper;
import com.school.communication.service.UserService;
import com.school.communication.utils.JwtUtil;
import com.school.communication.utils.PasswordUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private PasswordUtil passwordUtil;
    
    @Override
    public LoginResponseDTO login(LoginDTO loginDTO) {
        User user = baseMapper.selectByUsername(loginDTO.getUsername());
        
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }
        
        if (!passwordUtil.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        
        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setRealName(user.getRealName());
        response.setRole(user.getRole());
        response.setAvatar(user.getAvatar());
        
        return response;
    }
    
    @Override
    @Transactional
    public void register(UserDTO userDTO) {
        User existUser = baseMapper.selectByUsername(userDTO.getUsername());
        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setPassword(passwordUtil.encode(userDTO.getPassword()));
        user.setStatus(1);
        
        baseMapper.insert(user);
    }
    
    @Override
    public UserDTO getUserById(Long id) {
        User user = baseMapper.selectById(id);
        if (user == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);
        dto.setPassword(null);
        return dto;
    }
    
    @Override
    public List<UserDTO> getUserList(String role) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (role != null && !role.isEmpty()) {
            wrapper.eq(User::getRole, role);
        }
        wrapper.eq(User::getDeleted, 0);
        wrapper.orderByDesc(User::getCreateTime);
        
        List<User> users = baseMapper.selectList(wrapper);
        return users.stream().map(user -> {
            UserDTO dto = new UserDTO();
            BeanUtils.copyProperties(user, dto);
            dto.setPassword(null);
            return dto;
        }).collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void updateUser(UserDTO userDTO) {
        User user = baseMapper.selectById(userDTO.getId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        BeanUtils.copyProperties(userDTO, user);
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordUtil.encode(userDTO.getPassword()));
        }
        
        baseMapper.updateById(user);
    }
    
    @Override
    @Transactional
    public void deleteUser(Long id) {
        baseMapper.deleteById(id);
    }
    
    @Override
    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = baseMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        if (!passwordUtil.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        
        user.setPassword(passwordUtil.encode(newPassword));
        baseMapper.updateById(user);
    }
    
    @Override
    public Long verifyUser(String username, String phone) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username)
               .eq(User::getPhone, phone)
               .eq(User::getDeleted, 0);
        
        User user = baseMapper.selectOne(wrapper);
        
        if (user == null) {
            throw new RuntimeException("用户名或手机号不正确");
        }
        
        return user.getId();
    }
    
    @Override
    @Transactional
    public void resetPassword(Long userId, String newPassword) {
        User user = baseMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        user.setPassword(passwordUtil.encode(newPassword));
        baseMapper.updateById(user);
    }
}