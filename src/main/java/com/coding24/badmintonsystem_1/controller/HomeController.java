package com.coding24.badmintonsystem_1.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "redirect:/toLogin"; // 重定向到 /home
    }


    @GetMapping("/home")
    public String home(Model model) {
        // 获取当前用户信息
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("role", userDetails.getAuthorities().iterator().next().getAuthority());
        }
        model.addAttribute("activePage", "home");
        return "home"; // 返回 home.html
    }

    @GetMapping("/toLogin")
    public String login() {
        return "login"; // 返回 login.html
    }
}
