package com.coding24.badmintonsystem_1.service;

import com.coding24.badmintonsystem_1.entity.Feedback;
import java.util.List;

public interface FeedbackService {
    Feedback findById(Integer feedbackID);
    List<Feedback> findAll();
    void insertFeedback(Feedback feedback);
    void updateFeedback(Feedback feedback);
    void deleteFeedback(Integer feedbackID);
}
