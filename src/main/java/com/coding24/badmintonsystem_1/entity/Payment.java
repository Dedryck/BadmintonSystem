package com.coding24.badmintonsystem_1.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Payment {
    private Integer paymentID;
    private Integer bookingID;
    private Integer userID;
    private Double amount;
    private String paymentMethod;
    private String paymentStatus;
    private Date paymentDate;
}
