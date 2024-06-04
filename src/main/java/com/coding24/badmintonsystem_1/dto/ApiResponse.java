package com.coding24.badmintonsystem_1.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ApiResponse<T> {
    // Getters and Setters
    private int code;
    private String message;
    private List<T> data;
    private T singleData;

    public ApiResponse(int code, String message, List<T> data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(int code, String message, T singleData) {
        this.code = code;
        this.message = message;
        this.singleData = singleData;
    }

}
