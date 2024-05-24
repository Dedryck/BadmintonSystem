package com.coding24.badmintonsystem_1.controller;

import com.coding24.badmintonsystem_1.entity.EquipmentRental;
import com.coding24.badmintonsystem_1.service.EquipmentRentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class EquipmentRentalController {

    @Autowired
    private EquipmentRentalService equipmentRentalService;

    @GetMapping("/equipment-rentals")
    public String viewEquipmentRentals(Model model, @RequestParam(value = "message", required = false) String message) {
        List<EquipmentRental> equipmentRentals = equipmentRentalService.findAll();

        // 获取当前用户信息
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("role", auth.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .collect(Collectors.joining(", ")));
        }

        if (message != null) {
            model.addAttribute("message", message);
        }

        model.addAttribute("equipmentRentals", equipmentRentals);
        model.addAttribute("activePage", "equipmentRentals");
        return "equipment-rentals";
    }

    @PostMapping("/add-rental")
    public String addEquipmentRental(@ModelAttribute EquipmentRental equipmentRental,
                                     @RequestParam("image") MultipartFile imageFile,
                                     RedirectAttributes redirectAttributes) {
        // 保存图片
        if (!imageFile.isEmpty()) {
            try {
                String filename = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                String filepath = new File("src/main/resources/static/images").getAbsolutePath() + File.separator + filename;
                imageFile.transferTo(new File(filepath));
                equipmentRental.setImagePath("/images/" + filename); // 保证路径正确
            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("message", "图片上传失败");
                return "redirect:/equipment-rentals";
            }
        }

        // 获取当前用户信息并设置用户名
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            equipmentRental.setUsername(userDetails.getUsername());
        }

        // 插入租借信息到数据库
        equipmentRentalService.insertEquipmentRental(equipmentRental);
        redirectAttributes.addFlashAttribute("message", "租借成功");
        return "redirect:/equipment-rentals";
    }


}
