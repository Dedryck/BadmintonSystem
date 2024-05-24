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
@RequestMapping("/feedback-management")
public class FeedbackManagementController {

    private final FeedbackService feedbackService;
    private final UserService userService;

    @Autowired
    public FeedbackManagementController(FeedbackService feedbackService, UserService userService) {
        this.feedbackService = feedbackService;
        this.userService = userService;
    }

    @GetMapping
    public String viewFeedbackManagement(Model model, @ModelAttribute("message") String message) {
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
        if (message != null && !message.isEmpty()) {
            model.addAttribute("message", message); // 添加消息到模型
        }
        return "feedback-management";
    }

    @PostMapping("/add-feedbackmanagement")
    public String addFeedback(@ModelAttribute Feedback feedback, @RequestParam String username, RedirectAttributes redirectAttributes) {
        feedback.setUsername(username); // 设置用户名
        feedback.setFeedbackDate(new Date());
        feedbackService.insertFeedback(feedback);
        redirectAttributes.addFlashAttribute("message", "反馈添加成功！");
        return "redirect:/feedback-management";
    }

    @PostMapping("/edit-feedback")
    public String editFeedback(@ModelAttribute Feedback feedback, RedirectAttributes redirectAttributes) {
        feedback.setFeedbackDate(new Date());
        feedbackService.updateFeedback(feedback);
        redirectAttributes.addFlashAttribute("message", "反馈修改成功！");
        return "redirect:/feedback-management";
    }

    @DeleteMapping("/delete-feedback/{feedbackID}")
    @ResponseBody
    public String deleteFeedback(@PathVariable int feedbackID,RedirectAttributes redirectAttributes) {
        try {
            feedbackService.deleteFeedback(feedbackID);
            redirectAttributes.addFlashAttribute("message", "反馈删除成功！");

            return "success";
        } catch (Exception e) {
            return "failure";
        }
    }

    @GetMapping("/get-feedback/{feedbackID}")
    @ResponseBody
    public Feedback getFeedback(@PathVariable int feedbackID) {
        return feedbackService.findById(feedbackID);
    }
}
