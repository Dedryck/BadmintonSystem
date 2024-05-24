package com.coding24.badmintonsystem_1.controller;

import com.coding24.badmintonsystem_1.entity.Instructor;
import com.coding24.badmintonsystem_1.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class InstructorController {

    private final InstructorService instructorService;

    @Autowired
    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping("/instructors")
    public String getInstructors(Model model) {
        List<Instructor> instructors = instructorService.findAll();

        // 获取当前用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        model.addAttribute("username", username);
        model.addAttribute("role", role);
        model.addAttribute("instructors", instructors);
        model.addAttribute("activePage", "instructors");
        return "instructors";
    }
}
