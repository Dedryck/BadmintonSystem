package com.coding24.badmintonsystem_1.controller;

import com.coding24.badmintonsystem_1.dto.ApiResponse;
import com.coding24.badmintonsystem_1.entity.Court;
import com.coding24.badmintonsystem_1.service.CourtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/court-management")
public class CourtManagementController {

    @Autowired
    private CourtService courtService;

    @GetMapping
    public ApiResponse<List<Court>> viewCourtManagement() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        String role = null;
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            username = userDetails.getUsername();
            role = userDetails.getAuthorities().iterator().next().getAuthority();
        }

        List<Court> courts = courtService.findAll();
        return new ApiResponse<List<Court>>(0, "查询成功", courts);
    }

    @PostMapping("/add")
    public ApiResponse<Court> addCourt(@Valid @RequestBody Court court) {
        courtService.insertCourt(court);
        return new ApiResponse<>(0, "场地添加成功", court);
    }

    @PostMapping("/edit")
    public ApiResponse<Court> editCourt(@Valid @RequestBody Court court) {
        courtService.updateCourt(court);
        return new ApiResponse<>(0, "场地修改成功", court);
    }

    @DeleteMapping("/delete/{courtID}")
    public ApiResponse<Void> deleteCourt(@PathVariable Integer courtID) {
        courtService.deleteCourt(courtID);
        return new ApiResponse<>(0, "场地删除成功", null);
    }

    @GetMapping("/list")
    public ApiResponse<List<Court>> listCourts() {
        List<Court> courts = courtService.findAll();
        return new ApiResponse<List<Court>>(0, "查询成功", courts);
    }
}
