package com.coding24.badmintonsystem_1.service;

import com.coding24.badmintonsystem_1.entity.Court;

import java.util.List;

public interface CourtService {
    Court findById(Integer courtID);
    List<Court> findAll();
    void insertCourt(Court court);
    void updateCourt(Court court);
    void deleteCourt(Integer courtID);
}
