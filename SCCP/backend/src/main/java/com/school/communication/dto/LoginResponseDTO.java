package com.school.communication.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;
    private Long userId;
    private String username;
    private String realName;
    private String role;
    private String avatar;
}