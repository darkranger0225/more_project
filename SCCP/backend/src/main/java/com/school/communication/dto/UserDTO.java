package com.school.communication.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserDTO {
    private Long id;
    
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    private String password;
    
    @NotBlank(message = "真实姓名不能为空")
    private String realName;
    
    private String phone;
    
    private String avatar;
    
    @NotBlank(message = "角色不能为空")
    private String role;
    
    private Integer status;
}