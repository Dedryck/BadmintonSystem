package com.coding24.badmintonsystem_1.service.impl;

import com.coding24.badmintonsystem_1.entity.Feedback;
import com.coding24.badmintonsystem_1.mapper.FeedbackMapper;
import com.coding24.badmintonsystem_1.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackMapper feedbackMapper;

    @Autowired
    public FeedbackServiceImpl(FeedbackMapper feedbackMapper) {
        this.feedbackMapper = feedbackMapper;
    }

    @Override
    public Feedback findById(Integer feedbackID) {
        return feedbackMapper.findById(feedbackID);
    }

    @Override
    public List<Feedback> findAll() {
        return feedbackMapper.findAll();
    }

    @Override
    public void insertFeedback(Feedback feedback) {
        feedbackMapper.insertFeedback(feedback);
    }

    @Override
    public void updateFeedback(Feedback feedback) {
        feedbackMapper.updateFeedback(feedback);
    }

    @Override
    public void deleteFeedback(Integer feedbackID) {
        feedbackMapper.deleteFeedback(feedbackID);
    }
}
