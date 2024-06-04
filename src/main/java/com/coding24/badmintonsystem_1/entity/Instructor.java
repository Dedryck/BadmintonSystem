package com.coding24.badmintonsystem_1.entity;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class Instructor {
    private Integer instructorID;

    @NotBlank(message = "姓名不能为空")
    @Size(max = 30, message = "姓名长度不能超过30个字符")
    private String name;

    @NotBlank(message = "电话不能为空")
    @Pattern(regexp = "^\\d{10,20}$", message = "电话号码格式不正确，必须为10到15位数字")
    private String phone;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @NotBlank(message = "专长不能为空")
    @Size(max = 200, message = "专长长度不能超过200个字符")
    private String specialization;

    @NotNull(message = "可用状态不能为空")
    private Boolean available;

    @Pattern(regexp = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$", message = "照片路径格式不正确")
    private String photoPath;
}
