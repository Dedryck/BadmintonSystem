package com.coding24.badmintonsystem_1.controller;

import com.coding24.badmintonsystem_1.dto.ApiResponse;
import com.coding24.badmintonsystem_1.entity.User;
import com.coding24.badmintonsystem_1.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> registerUser(@Valid @RequestBody User user) {
        if (userService.findByUsername(user.getUsername()) != null) {
            ApiResponse<User> response = new ApiResponse<>(1, "用户名已注册", Collections.emptyList());
            return ResponseEntity.badRequest().body(response);
        }

        userService.insertUser(user);
        ApiResponse<User> response = new ApiResponse<>(0, "注册成功", Collections.singletonList(user));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-username")
    public ResponseEntity<ApiResponse<Boolean>> checkUsername(@RequestParam String username) {
        boolean exists = userService.findByUsername(username) != null;
        ApiResponse<Boolean> response = new ApiResponse<>(0, "查询成功", Collections.singletonList(exists));
        return ResponseEntity.ok(response);
    }
}

