package com.example.electrical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.electrical.common.Constants;
import com.example.electrical.common.Result;
import com.example.electrical.dto.*;
import com.example.electrical.entity.Dormitory;
import com.example.electrical.entity.Floor;
import com.example.electrical.entity.User;
import com.example.electrical.mapper.DormitoryMapper;
import com.example.electrical.mapper.FloorMapper;
import com.example.electrical.mapper.UserMapper;
import com.example.electrical.service.UserService;
import com.example.electrical.util.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FloorMapper floorMapper;

    @Autowired
    private DormitoryMapper dormitoryMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Result<String> login(LoginDTO loginDTO) {
        User user = userMapper.selectByAccount(loginDTO.getAccount());
        if (user == null) {
            return Result.error("账号或密码错误");
        }

        if (user.getStatus() == Constants.USER_STATUS_DISABLED) {
            return Result.error("账号已被禁用");
        }

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return Result.error("账号或密码错误");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getAccount(), user.getRole());
        return Result.success(token);
    }

    @Override
    public Result<Map<String, Object>> loginWithRole(LoginDTO loginDTO) {
        User user = userMapper.selectByAccount(loginDTO.getAccount());
        if (user == null) {
            return Result.error("账号或密码错误");
        }

        if (user.getStatus() == Constants.USER_STATUS_DISABLED) {
            return Result.error("账号已被禁用");
        }

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return Result.error("账号或密码错误");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getAccount(), user.getRole());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("role", user.getRole());
        result.put("message", "登录成功");

        return Result.success(result);
    }

    @Override
    public Result<Void> register(RegisterDTO registerDTO) {
        // 检查账号是否已存在
        User existUser = userMapper.selectByAccount(registerDTO.getAccount());
        if (existUser != null) {
            return Result.error("账号已存在");
        }

        // 检查宿舍是否存在
        Dormitory dormitory = dormitoryMapper.selectById(registerDTO.getDormitoryId());
        if (dormitory == null) {
            return Result.error("宿舍不存在");
        }

        // 检查宿舍是否属于该楼层
        if (!dormitory.getFloorId().equals(registerDTO.getFloorId())) {
            return Result.error("宿舍与楼层不匹配");
        }

        User user = new User();
        BeanUtils.copyProperties(registerDTO, user);
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRole(Constants.ROLE_STUDENT);
        user.setStatus(Constants.USER_STATUS_ENABLED);

        userMapper.insert(user);
        return Result.success();
    }

    @Override
    public Result<UserInfoDTO> getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(user, userInfoDTO);

        // 查询宿舍信息
        if (user.getDormitoryId() != null) {
            Dormitory dormitory = dormitoryMapper.selectById(user.getDormitoryId());
            if (dormitory != null) {
                userInfoDTO.setDormitoryNumber(dormitory.getDormitoryNumber() != null ? dormitory.getDormitoryNumber() : "");
                // 根据宿舍信息构建楼栋信息
                if (dormitory.getBuildingId() != null) {
                    String buildingInfo = dormitory.getBuildingId().toString() + "栋";
                    userInfoDTO.setBuildingInfo(buildingInfo);
                } else {
                    userInfoDTO.setBuildingInfo("");
                }
            } else {
                userInfoDTO.setDormitoryNumber("");
                userInfoDTO.setBuildingInfo("");
            }
        } else {
            userInfoDTO.setDormitoryNumber("");
            userInfoDTO.setBuildingInfo("");
        }

        // 查询楼层信息（如果宿舍信息没有构建成功）
        if (userInfoDTO.getBuildingInfo() == null || userInfoDTO.getBuildingInfo().isEmpty()) {
            if (user.getFloorId() != null) {
                Floor floor = floorMapper.selectById(user.getFloorId());
                if (floor != null) {
                    userInfoDTO.setFloorNumber(floor.getFloorNumber());
                    // 构建楼栋信息，如 "1栋"
                    if (floor.getBuildingId() != null) {
                        String buildingInfo = floor.getBuildingId().toString() + "栋";
                        userInfoDTO.setBuildingInfo(buildingInfo);
                    } else {
                        userInfoDTO.setBuildingInfo("");
                    }
                } else {
                    userInfoDTO.setBuildingInfo("");
                }
            } else {
                userInfoDTO.setBuildingInfo("");
            }
        }

        return Result.success(userInfoDTO);
    }

    @Override
    public Result<Void> updateUserInfo(Long userId, UserInfoDTO userInfoDTO) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }

        // 只允许更新部分字段
        user.setName(userInfoDTO.getName());
        user.setGender(userInfoDTO.getGender());
        user.setAge(userInfoDTO.getAge());
        user.setPhone(userInfoDTO.getPhone());
        user.setPhoto(userInfoDTO.getPhoto());

        userMapper.updateById(user);
        return Result.success();
    }

    @Override
    public Result<Void> updatePassword(Long userId, PasswordDTO passwordDTO) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }

        if (!passwordEncoder.matches(passwordDTO.getOldPassword(), user.getPassword())) {
            return Result.error("旧密码错误");
        }

        user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        userMapper.updateById(user);
        return Result.success();
    }

    @Override
    public Result<Void> resetPassword(String account, String phone, String newPassword) {
        // 根据账号查询用户
        User user = userMapper.selectByAccount(account);
        if (user == null) {
            return Result.error("用户不存在");
        }

        // 验证手机号是否匹配
        if (user.getPhone() == null || !user.getPhone().equals(phone)) {
            return Result.error("手机号验证失败");
        }

        // 验证新密码长度
        if (newPassword == null || newPassword.length() < 6) {
            return Result.error("新密码至少6位");
        }

        // 加密新密码并更新
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updateById(user);
        return Result.success();
    }
}
