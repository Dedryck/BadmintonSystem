package com.coding24.badmintonsystem_1.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class Booking {
    private Integer bookingID;
    private Integer userID;
    private Integer courtID;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date endTime;
    private Date bookingDate;
    private String status;
}
