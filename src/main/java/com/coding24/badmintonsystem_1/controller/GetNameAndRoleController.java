package com.coding24.badmintonsystem_1.controller;

import com.coding24.badmintonsystem_1.entity.User;
import com.coding24.badmintonsystem_1.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GetNameAndRoleController {

    private final UserService userService;

    @Autowired
    public GetNameAndRoleController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user-info")
    public UserInfo getUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = new UserInfo();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            User user = userService.findByUsername(userDetails.getUsername());
            if (user != null) {
                userInfo.setUsername(user.getUsername());
                userInfo.setRole(user.getRole());
                userInfo.setUserID(userService.getUserIDByUsername(user.getUsername()));
            }
        }
        return userInfo;
    }

    @Setter
    @Getter
    static class UserInfo {
        private String username;
        private String role;
        private Integer userID;
    }
}
