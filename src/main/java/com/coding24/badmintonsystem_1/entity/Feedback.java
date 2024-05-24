package com.coding24.badmintonsystem_1.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Feedback {
    private Integer feedbackID;
    private Integer userID;
    private String comments;
    private Integer rating;
    private Date feedbackDate;
    private String username;
}
