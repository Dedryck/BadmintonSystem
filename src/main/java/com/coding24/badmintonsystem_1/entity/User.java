package com.coding24.badmintonsystem_1.entity;

import lombok.Data;

@Data
public class User {
    private Integer userID;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String role;
}
