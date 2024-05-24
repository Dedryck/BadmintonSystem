package com.coding24.badmintonsystem_1.entity;

import lombok.Data;

@Data
public class Instructor {
    private Integer instructorID;
    private String name;
    private String phone;
    private String email;
    private String specialization;
    private Boolean available;
    private String photoPath;
}
