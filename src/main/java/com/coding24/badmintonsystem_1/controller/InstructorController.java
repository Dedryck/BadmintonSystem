package com.coding24.badmintonsystem_1.controller;

import com.coding24.badmintonsystem_1.dto.ApiResponse;
import com.coding24.badmintonsystem_1.entity.Instructor;
import com.coding24.badmintonsystem_1.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
public class InstructorController {

    private final InstructorService instructorService;

    @Autowired
    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping
    public ApiResponse<List<Instructor>> getInstructors() {
        List<Instructor> instructors = instructorService.findAll();

        // 获取当前用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        String role = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
            role = userDetails.getAuthorities().iterator().next().getAuthority();
        }

        return new ApiResponse<List<Instructor>>(0, "查询成功", instructors);
    }
}
