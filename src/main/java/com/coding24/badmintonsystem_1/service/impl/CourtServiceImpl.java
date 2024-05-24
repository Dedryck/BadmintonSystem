package com.coding24.badmintonsystem_1.service.impl;

import com.coding24.badmintonsystem_1.entity.Court;
import com.coding24.badmintonsystem_1.mapper.CourtMapper;
import com.coding24.badmintonsystem_1.service.CourtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourtServiceImpl implements CourtService {

    private final CourtMapper courtMapper;

    @Autowired
    public CourtServiceImpl(CourtMapper courtMapper) {
        this.courtMapper = courtMapper;
    }

    @Override
    public Court findById(Integer courtID) {
        return courtMapper.findById(courtID);
    }

    @Override
    public List<Court> findAll() {
        return courtMapper.findAll();
    }

    @Override
    public void insertCourt(Court court) {
        courtMapper.insertCourt(court);
    }

    @Override
    public void updateCourt(Court court) {
        courtMapper.updateCourt(court);
    }

    @Override
    public void deleteCourt(Integer courtID) {
        courtMapper.deleteCourt(courtID);
    }
}
