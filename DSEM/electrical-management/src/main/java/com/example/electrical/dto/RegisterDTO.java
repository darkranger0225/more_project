package com.example.electrical.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDTO {

    @NotBlank(message = "账号不能为空")
    @Size(min = 3, max = 20, message = "账号长度必须在3-20位之间")
    private String account;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20位之间")
    private String password;

    @NotBlank(message = "姓名不能为空")
    private String name;

    @NotNull(message = "楼层不能为空")
    private Long floorId;

    @NotNull(message = "宿舍不能为空")
    private Long dormitoryId;

    private Integer gender;

    private Integer age;

    @Pattern(regexp = "^(|1[3-9]\\d{9})$", message = "手机号格式不正确")
    private String phone;
}
