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
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/equipment-management")
public class EquipmentManagementController {

    private final EquipmentRentalService equipmentRentalService;

    @Autowired
    public EquipmentManagementController(EquipmentRentalService equipmentRentalService) {
        this.equipmentRentalService = equipmentRentalService;
    }

    @GetMapping
    public ApiResponse<List<EquipmentRental>> viewEquipmentManagement() {
        List<EquipmentRental> equipmentList = equipmentRentalService.findAll();
        return new ApiResponse<List<EquipmentRental>>(0, "查询成功", equipmentList);
    }


    @PostMapping("/add")
    public ApiResponse<EquipmentRental> addEquipment(@Valid @ModelAttribute EquipmentRental equipmentRental,
                                                     @RequestParam("image") MultipartFile imageFile) {

        // 添加日志记录以调试
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
                return new ApiResponse<>(1, "图片上传失败", (EquipmentRental) null);
            }
        }

        // 获取当前用户信息并设置用户名
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            equipmentRental.setUsername(userDetails.getUsername());
        }

        // 插入设备信息到数据库
        equipmentRentalService.insertEquipmentRental(equipmentRental);
        return new ApiResponse<>(0, "设备添加成功！", equipmentRental);
    }

    @PostMapping("/edit")
    public ApiResponse<EquipmentRental> editEquipment(@Valid @ModelAttribute EquipmentRental equipmentRental,
                                                      @RequestParam("image") MultipartFile imageFile) {
        // 获取当前用户信息并设置用户名
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            equipmentRental.setUsername(userDetails.getUsername());
        }

        try {
            if (!imageFile.isEmpty()) {
                String filename = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                String filepath = new File("src/main/resources/static/images").getAbsolutePath() + File.separator + filename;
                imageFile.transferTo(new File(filepath));
                equipmentRental.setImagePath("/images/" + filename);
            }
            equipmentRentalService.updateEquipmentRental(equipmentRental);
            return new ApiResponse<>(0, "设备修改成功！", equipmentRental);
        } catch (IOException e) {
            e.printStackTrace();
            return new ApiResponse<>(1, "设备修改失败：" + e.getMessage(), (EquipmentRental) null);
        }
    }

    @DeleteMapping("/delete/{rentalID}")
    public ApiResponse<Void> deleteEquipment(@Valid @PathVariable int rentalID) {
        try {
            equipmentRentalService.deleteEquipmentRental(rentalID);
            return new ApiResponse<>(0, "删除成功", (Void) null);
        } catch (Exception e) {
            return new ApiResponse<>(1, "删除失败：" + e.getMessage(), (Void) null);
        }
    }

    @GetMapping("/get/{rentalID}")
    public ApiResponse<EquipmentRental> getEquipment(@PathVariable int rentalID) {
        EquipmentRental equipmentRental = equipmentRentalService.findById(rentalID);
        return new ApiResponse<>(0, "查询成功", equipmentRental);
    }

}
