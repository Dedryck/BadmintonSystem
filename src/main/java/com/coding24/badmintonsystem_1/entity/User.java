package com.coding24.badmintonsystem_1.entity;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class User {

    private Integer userID;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 30, message = "用户名长度必须在3到30之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码长度必须至少6位")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Pattern(regexp = "^\\d{10,20}$", message = "电话号码格式不正确")
    private String phone;

    @NotNull(message = "角色不能为空")
    private String role;
}
