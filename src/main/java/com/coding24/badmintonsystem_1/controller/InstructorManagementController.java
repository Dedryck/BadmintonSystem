package com.coding24.badmintonsystem_1.controller;

import com.coding24.badmintonsystem_1.entity.Instructor;
import com.coding24.badmintonsystem_1.service.InstructorService;
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

@Controller
public class InstructorManagementController {

    private final InstructorService instructorService;

    @Autowired
    public InstructorManagementController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping("/instructor-management")
    public String viewInstructorManagement(Model model) {
        List<Instructor> instructorList = instructorService.findAll();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("username", username);
        model.addAttribute("role", role);
        model.addAttribute("instructors", instructorList);
        model.addAttribute("activePage", "instructorManagement");
        return "instructor-management";
    }

    @PostMapping("/add-instructor")
    public String addInstructor(@ModelAttribute Instructor instructor,
                                @RequestParam("photo") MultipartFile photoFile,
                                RedirectAttributes redirectAttributes) {
        // 保存照片
        if (!photoFile.isEmpty()) {
            try {
                String filename = UUID.randomUUID().toString() + "_" + photoFile.getOriginalFilename();
                String filepath = new File("src/main/resources/static/images").getAbsolutePath() + File.separator + filename;
                photoFile.transferTo(new File(filepath));
                instructor.setPhotoPath("/images/" + filename); // 保证路径正确
            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("message", "照片上传失败");
                return "redirect:/instructor-management";
            }
        }

        // 插入教练信息到数据库
        instructorService.insertInstructor(instructor);
        redirectAttributes.addFlashAttribute("message", "教练添加成功！");
        return "redirect:/instructor-management";
    }

    @PostMapping("/edit-instructor")
    public String editInstructor(@ModelAttribute Instructor instructor,
                                 @RequestParam("photo") MultipartFile photoFile,
                                 RedirectAttributes redirectAttributes) {
        try {
            if (!photoFile.isEmpty()) {
                String filename = UUID.randomUUID().toString() + "_" + photoFile.getOriginalFilename();
                String filepath = new File("src/main/resources/static/images").getAbsolutePath() + File.separator + filename;
                photoFile.transferTo(new File(filepath));
                instructor.setPhotoPath("/images/" + filename);
            }
            instructorService.updateInstructor(instructor);
            redirectAttributes.addFlashAttribute("message", "教练修改成功！");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "教练修改失败：" + e.getMessage());
        }
        return "redirect:/instructor-management";
    }

    @DeleteMapping("/delete-instructor/{instructorID}")
    @ResponseBody
    public String deleteInstructor(@PathVariable int instructorID) {
        try {
            instructorService.deleteInstructor(instructorID);
            return "success";
        } catch (Exception e) {
            return "failure";
        }
    }

    @GetMapping("/get-instructor/{instructorID}")
    @ResponseBody
    public Instructor getInstructor(@PathVariable int instructorID) {
        return instructorService.findById(instructorID);
    }
}
