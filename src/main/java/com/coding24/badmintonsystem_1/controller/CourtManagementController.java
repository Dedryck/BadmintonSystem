package com.coding24.badmintonsystem_1.controller;

import com.coding24.badmintonsystem_1.entity.Court;
import com.coding24.badmintonsystem_1.service.CourtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/court-management")
public class CourtManagementController {

    @Autowired
    private CourtService courtService;

    @GetMapping
    public String viewCourtManagement(Model model, @RequestParam(value = "message", required = false) String message) {
        // 获取当前用户信息
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("role", userDetails.getAuthorities().iterator().next().getAuthority());
        }

        List<Court> courts = courtService.findAll();
        model.addAttribute("courts", courts);
        model.addAttribute("message", message);
        model.addAttribute("activePage", "courtManagement");
        return "court-management";
    }

    @PostMapping("/add")
    @ResponseBody
    public String addCourt(@ModelAttribute Court court) {
        courtService.insertCourt(court);
        return "{\"message\": \"场地添加成功\"}";
    }

    @PostMapping("/edit")
    @ResponseBody
    public String editCourt(@ModelAttribute Court court) {
        courtService.updateCourt(court);
        return "{\"message\": \"场地修改成功\"}";
    }

    @DeleteMapping("/delete/{courtID}")
    @ResponseBody
    public String deleteCourt(@PathVariable Integer courtID) {
        courtService.deleteCourt(courtID);
        return "{\"message\": \"场地删除成功\"}";
    }

    @GetMapping("/list")
    public String listCourts(Model model) {
        List<Court> courts = courtService.findAll();
        model.addAttribute("courts", courts);
        return "court-management :: courtTableBody";
    }
}
