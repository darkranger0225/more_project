package com.example.electrical.service;

import com.example.electrical.dto.*;
import com.example.electrical.common.Result;

import java.util.Map;

public interface UserService {

    Result<String> login(LoginDTO loginDTO);

    Result<Map<String, Object>> loginWithRole(LoginDTO loginDTO);

    Result<Void> register(RegisterDTO registerDTO);

    Result<UserInfoDTO> getUserInfo(Long userId);

    Result<Void> updateUserInfo(Long userId, UserInfoDTO userInfoDTO);

    Result<Void> updatePassword(Long userId, PasswordDTO passwordDTO);

    Result<Void> resetPassword(String account, String phone, String newPassword);
}
