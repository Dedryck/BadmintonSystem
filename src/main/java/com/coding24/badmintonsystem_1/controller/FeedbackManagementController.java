package com.coding24.badmintonsystem_1.controller;

import com.coding24.badmintonsystem_1.dto.ApiResponse;
import com.coding24.badmintonsystem_1.entity.Feedback;
import com.coding24.badmintonsystem_1.service.FeedbackService;
import com.coding24.badmintonsystem_1.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/feedback-management")
@PreAuthorize("hasRole('ADMIN')")
public class FeedbackManagementController {

    private final FeedbackService feedbackService;
    private final UserService userService;

    @Autowired
    public FeedbackManagementController(FeedbackService feedbackService, UserService userService) {
        this.feedbackService = feedbackService;
        this.userService = userService;
    }

    @GetMapping
    public ApiResponse<List<Feedback>> viewFeedbackManagement() {
        List<Feedback> feedbackList = feedbackService.findAll();
        return new ApiResponse<List<Feedback>>(0, "查询成功", feedbackList);
    }

    @PostMapping("/add")
    public ApiResponse<Feedback> addFeedback(@Valid @RequestBody Feedback feedback, @RequestParam String username) {
        feedback.setUsername(username); // 设置用户名
        feedback.setFeedbackDate(new Date());
        feedbackService.insertFeedback(feedback);
        return new ApiResponse<>(0, "反馈添加成功！", feedback);
    }

    @PostMapping("/edit")
    public ApiResponse<Feedback> editFeedback(@Valid @RequestBody Feedback feedback) {
        feedback.setFeedbackDate(new Date());
        feedbackService.updateFeedback(feedback);
        return new ApiResponse<>(0, "反馈修改成功！", feedback);
    }

    @DeleteMapping("/delete/{feedbackID}")
    public ApiResponse<Void> deleteFeedback(@PathVariable int feedbackID) {
        try {
            feedbackService.deleteFeedback(feedbackID);
            return new ApiResponse<>(0, "反馈删除成功！", null);
        } catch (Exception e) {
            return new ApiResponse<>(1, "反馈删除失败：" + e.getMessage(), null);
        }
    }

    @GetMapping("/get/{feedbackID}")
    public ApiResponse<Feedback> getFeedback(@PathVariable int feedbackID) {
        Feedback feedback = feedbackService.findById(feedbackID);
        return new ApiResponse<>(0, "查询成功", feedback);
    }
}
