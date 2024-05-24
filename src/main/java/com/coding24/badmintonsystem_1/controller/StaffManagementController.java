package com.coding24.badmintonsystem_1.controller;

import com.coding24.badmintonsystem_1.entity.User;
import com.coding24.badmintonsystem_1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/staff-management")
public class StaffManagementController {

    private final UserService userService;

    @Autowired
    public StaffManagementController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String staffManagementPage(Model model) {
        addUserInfoToModel(model); // 添加当前用户信息到模型
        model.addAttribute("userList", userService.findAllUsers());
        model.addAttribute("activePage", "staffManagement");
        return "staff-management";
    }

    @PostMapping("/add-user")
    public String addUser(User user, Model model) {
        userService.insertUser(user);
        model.addAttribute("message", "人员添加成功！");
        return "redirect:/staff-management";
    }

    @PostMapping("/edit-user")
    public String editUser(User user, Model model) {
        userService.updateUser(user);
        model.addAttribute("message", "人员修改成功！");
        return "redirect:/staff-management";
    }

    @DeleteMapping("/delete-user/{id}")
    @ResponseBody
    public String deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return "人员删除成功！";
    }

    @GetMapping("/get-user/{id}")
    @ResponseBody
    public User getUser(@PathVariable("id") Integer id) {
        return userService.findUserById(id);
    }
    // 添加一个辅助方法来获取当前用户信息并添加到模型中
    private void addUserInfoToModel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("role", userDetails.getAuthorities().iterator().next().getAuthority());
        }
    }

}
