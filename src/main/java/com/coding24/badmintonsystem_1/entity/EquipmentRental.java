package com.coding24.badmintonsystem_1.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class EquipmentRental {
    private Integer rentalID;
    private String equipmentType;
    private Integer quantity;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date rentalDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date returnDate;
    private String status;
    private String username; // 更新为用户名
    private String imagePath; // 新增字段
}
