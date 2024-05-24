package com.coding24.badmintonsystem_1.controller;

import com.coding24.badmintonsystem_1.entity.Feedback;
import com.coding24.badmintonsystem_1.service.FeedbackService;
import com.coding24.badmintonsystem_1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final UserService userService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService, UserService userService) {
        this.feedbackService = feedbackService;
        this.userService = userService;
    }

    @GetMapping("/feedback")
    public String viewFeedbackPage(Model model, @ModelAttribute("message") String message) {
        List<Feedback> feedbackList = feedbackService.findAll();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Integer userID = userService.findByUsername(username).getUserID();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("username", username);
        model.addAttribute("role", role);
        model.addAttribute("userID", userID);
        model.addAttribute("feedbacks", feedbackList);
        model.addAttribute("newFeedback", new Feedback());
        model.addAttribute("message", message); // 添加消息到模型
        return "feedback";
    }

    @PostMapping("/add-feedback")
    public String addFeedback(@ModelAttribute Feedback feedback, @RequestParam String username, RedirectAttributes redirectAttributes) {
        feedback.setUsername(username); // 设置用户名
        feedback.setFeedbackDate(new Date());
        feedbackService.insertFeedback(feedback);
        redirectAttributes.addFlashAttribute("message", "反馈添加成功！");
        return "redirect:/feedback";
    }
}
