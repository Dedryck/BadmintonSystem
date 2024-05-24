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
import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class EquipmentManagementController {

    private final EquipmentRentalService equipmentRentalService;

    @Autowired
    public EquipmentManagementController(EquipmentRentalService equipmentRentalService) {
        this.equipmentRentalService = equipmentRentalService;
    }

    @GetMapping("/equipment-management")
    public String viewEquipmentManagement(Model model) {
        List<EquipmentRental> equipmentList = equipmentRentalService.findAll();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("username", username);
        model.addAttribute("role", role);
        model.addAttribute("equipmentList", equipmentList);
        model.addAttribute("activePage", "equipmentManagement");
        return "equipment-management";
    }

    @PostMapping("/add-equipment")
    public String addEquipment(@ModelAttribute EquipmentRental equipmentRental,
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
                return "redirect:/equipment-management";
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
        redirectAttributes.addFlashAttribute("message", "设备添加成功！");
        return "redirect:/equipment-management";
    }

    @PostMapping("/edit-equipment")
    public String editEquipment(@ModelAttribute EquipmentRental equipmentRental,
                                @RequestParam("image") MultipartFile imageFile,
                                RedirectAttributes redirectAttributes) {
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
            redirectAttributes.addFlashAttribute("message", "设备修改成功！");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "设备修改失败：" + e.getMessage());
        }
        return "redirect:/equipment-management";
    }

    @DeleteMapping("/delete-equipment/{rentalID}")
    @ResponseBody
    public String deleteEquipment(@PathVariable int rentalID) {
        try {
            equipmentRentalService.deleteEquipmentRental(rentalID);
            return "success";
        } catch (Exception e) {
            return "failure";
        }
    }

    @GetMapping("/get-equipment/{rentalID}")
    @ResponseBody
    public EquipmentRental getEquipment(@PathVariable int rentalID) {
        return equipmentRentalService.findById(rentalID);
    }

    private String saveImage(MultipartFile image) throws IOException {
        String imagePath = "src/main/resources/static/images/" + image.getOriginalFilename();
        File file = new File(imagePath);
        image.transferTo(file);
        return "/images/" + image.getOriginalFilename();
    }
}
