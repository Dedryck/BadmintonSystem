package com.coding24.badmintonsystem_1.controller;

import com.coding24.badmintonsystem_1.dto.ApiResponse;
import com.coding24.badmintonsystem_1.entity.User;
import com.coding24.badmintonsystem_1.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff-management")

public class StaffManagementController {

    private final UserService userService;

    @Autowired
    public StaffManagementController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ApiResponse<List<User>> staffManagementPage() {
        List<User> userList = userService.findAllUsers();
        return new ApiResponse<List<User>>(0, "查询成功", userList);
    }

    @PostMapping("/add-user")
    public ApiResponse<User> addUser(@Valid @RequestBody User user) {
        userService.insertUser(user);
        return new ApiResponse<>(0, "人员添加成功", user);
    }

    @PostMapping("/edit-user")
    public ApiResponse<User> editUser(@Valid @RequestBody User user) {
        userService.updateUser(user);
        return new ApiResponse<>(0, "人员修改成功", user);
    }

    @DeleteMapping("/delete-user/{id}")
    public ApiResponse<Void> deleteUser(@Valid @PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return new ApiResponse<>(0, "人员删除成功", null);
    }

    @GetMapping("/get-user/{id}")
    public ApiResponse<User> getUser(@Valid @PathVariable("id") Integer id) {
        User user = userService.findUserById(id);
        return new ApiResponse<>(0, "查询成功", user);
    }
}
