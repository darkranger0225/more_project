package com.school.communication.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.school.communication.dto.LoginDTO;
import com.school.communication.dto.LoginResponseDTO;
import com.school.communication.dto.UserDTO;
import com.school.communication.entity.User;

import java.util.List;

public interface UserService extends IService<User> {
    
    LoginResponseDTO login(LoginDTO loginDTO);
    
    void register(UserDTO userDTO);
    
    UserDTO getUserById(Long id);
    
    List<UserDTO> getUserList(String role);
    
    void updateUser(UserDTO userDTO);
    
    void deleteUser(Long id);
    
    void changePassword(Long userId, String oldPassword, String newPassword);
    
    Long verifyUser(String username, String phone);
    
    void resetPassword(Long userId, String newPassword);
}