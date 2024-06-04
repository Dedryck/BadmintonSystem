package com.coding24.badmintonsystem_1.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class EquipmentRental {
    private Integer rentalID;//唯一标识符

    @NotBlank(message = "设备类型不能为空")
    private String equipmentType;

    @NotNull(message = "数量不能为空")
    @Min(value = 0, message = "数量不能为负数")
    private Integer quantity;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date rentalDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date returnDate;

    @NotBlank(message = "状态不能为空")
    private String status;

    @NotBlank(message = "用户名不能为空")
    private String username; // 更新为用户名

    @Pattern(regexp = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$", message = "图片路径格式不正确")
    private String imagePath; // 新增字段
}
