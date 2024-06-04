package com.coding24.badmintonsystem_1.controller;

import com.coding24.badmintonsystem_1.dto.ApiResponse;
import com.coding24.badmintonsystem_1.entity.Feedback;
import com.coding24.badmintonsystem_1.service.FeedbackService;
import com.coding24.badmintonsystem_1.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final UserService userService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService, UserService userService) {
        this.feedbackService = feedbackService;
        this.userService = userService;
    }

    @GetMapping
    public ApiResponse<List<Feedback>> viewFeedback() {
        List<Feedback> feedbackList = feedbackService.findAll();
        return new ApiResponse<List<Feedback>>(0, "查询成功", feedbackList);
    }

    @PostMapping("/add")
    public ApiResponse<Feedback> addFeedback(@Valid @RequestBody Feedback feedback, @RequestParam String username) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            feedback.setUsername(userDetails.getUsername());
        }
        feedback.setFeedbackDate(new Date());
        feedbackService.insertFeedback(feedback);
        return new ApiResponse<>(0, "反馈添加成功", feedback);
    }
}

