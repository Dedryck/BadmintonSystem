package com.coding24.badmintonsystem_1.controller;

import com.coding24.badmintonsystem_1.dto.ApiResponse;
import com.coding24.badmintonsystem_1.entity.Instructor;
import com.coding24.badmintonsystem_1.service.InstructorService;
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
@RequestMapping("/api/instructor-management")
public class InstructorManagementController {

    private final InstructorService instructorService;

    @Autowired
    public InstructorManagementController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping
    public ApiResponse<List<Instructor>> viewInstructorManagement() {
        List<Instructor> instructorList = instructorService.findAll();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        String role = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
            role = userDetails.getAuthorities().iterator().next().getAuthority();
        }
        return new ApiResponse<List<Instructor>>(0, "查询成功", instructorList);
    }

    @PostMapping("/add")
    public ApiResponse<Instructor> addInstructor(@Valid @ModelAttribute Instructor instructor,
                                                 @RequestParam("photo") MultipartFile photoFile) {
        // 保存照片
        if (!photoFile.isEmpty()) {
            try {
                String filename = UUID.randomUUID().toString() + "_" + photoFile.getOriginalFilename();
                String filepath = new File("src/main/resources/static/images").getAbsolutePath() + File.separator + filename;
                photoFile.transferTo(new File(filepath));
                instructor.setPhotoPath("/images/" + filename); // 保证路径正确
            } catch (IOException e) {
                e.printStackTrace();
                return new ApiResponse<>(1, "照片上传失败", null);
            }
        }

        // 插入教练信息到数据库
        instructorService.insertInstructor(instructor);
        return new ApiResponse<>(0, "教练添加成功", instructor);
    }

    @PostMapping("/edit")
    public ApiResponse<Instructor> editInstructor(@Valid @ModelAttribute Instructor instructor,
                                                  @RequestParam("photo") MultipartFile photoFile) {
        try {
            if (!photoFile.isEmpty()) {
                String filename = UUID.randomUUID().toString() + "_" + photoFile.getOriginalFilename();
                String filepath = new File("src/main/resources/static/images").getAbsolutePath() + File.separator + filename;
                photoFile.transferTo(new File(filepath));
                instructor.setPhotoPath("/images/" + filename);
            }
            instructorService.updateInstructor(instructor);
            return new ApiResponse<>(0, "教练修改成功", instructor);
        } catch (IOException e) {
            e.printStackTrace();
            return new ApiResponse<>(1, "教练修改失败：" + e.getMessage(), null);
        }
    }

    @DeleteMapping("/delete/{instructorID}")
    public ApiResponse<Void> deleteInstructor(@PathVariable int instructorID) {
        try {
            instructorService.deleteInstructor(instructorID);
            return new ApiResponse<>(0, "删除成功", null);
        } catch (Exception e) {
            return new ApiResponse<>(1, "删除失败：" + e.getMessage(), null);
        }
    }

    @GetMapping("/get/{instructorID}")
    public ApiResponse<Instructor> getInstructor(@PathVariable int instructorID) {
        Instructor instructor = instructorService.findById(instructorID);
        return new ApiResponse<>(0, "查询成功", instructor);
    }
}
