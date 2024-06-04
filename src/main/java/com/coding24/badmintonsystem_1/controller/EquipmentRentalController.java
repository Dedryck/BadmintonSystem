package com.coding24.badmintonsystem_1.controller;

import com.coding24.badmintonsystem_1.dto.ApiResponse;
import com.coding24.badmintonsystem_1.entity.EquipmentRental;
import com.coding24.badmintonsystem_1.service.EquipmentRentalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/equipment-rentals")
public class EquipmentRentalController {

    @Autowired
    private EquipmentRentalService equipmentRentalService;

    @GetMapping
    public ApiResponse<EquipmentRental> getAllRentals() {
        List<EquipmentRental> rentals = equipmentRentalService.findAll();
        return new ApiResponse<>(0, "查询成功", rentals);
    }

    @PostMapping
    public ApiResponse<EquipmentRental> addEquipmentRental(@Valid @ModelAttribute EquipmentRental equipmentRental,
                                                           @RequestParam("image") MultipartFile imageFile) {
        List<EquipmentRental> responseData = new ArrayList<>();

        System.out.println("Received EquipmentRental: " + equipmentRental);
        System.out.println("Received Image File: " + imageFile.getOriginalFilename());

        // 保存图片
        if (!imageFile.isEmpty()) {
            try {
                String filename = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                String filepath = new File("src/main/resources/static/images").getAbsolutePath() + File.separator + filename;
                imageFile.transferTo(new File(filepath));
                equipmentRental.setImagePath("/images/" + filename); // 保证路径正确
            } catch (IOException e) {
                e.printStackTrace();
                return new ApiResponse<>(1, "图片上传失败", null);
            }
        }

        // 获取当前用户信息并设置用户名
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            equipmentRental.setUsername(userDetails.getUsername());
        }
        // 检查状态值
        if (!equipmentRental.getStatus().equals("rented") && !equipmentRental.getStatus().equals("returned")) {
            return new ApiResponse<>(1, "无效的状态值", null);
        }

        // 插入租借信息到数据库
        equipmentRentalService.insertEquipmentRental(equipmentRental);
        responseData.add(equipmentRental);
        return new ApiResponse<>(0, "租借成功", responseData);
    }
}
