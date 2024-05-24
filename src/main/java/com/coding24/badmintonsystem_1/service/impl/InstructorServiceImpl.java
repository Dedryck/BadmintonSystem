package com.coding24.badmintonsystem_1.service.impl;

import com.coding24.badmintonsystem_1.entity.Instructor;
import com.coding24.badmintonsystem_1.mapper.InstructorMapper;
import com.coding24.badmintonsystem_1.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstructorServiceImpl implements InstructorService {

    private final InstructorMapper instructorMapper;

    @Autowired
    public InstructorServiceImpl(InstructorMapper instructorMapper) {
        this.instructorMapper = instructorMapper;
    }

    @Override
    public Instructor findById(Integer instructorID) {
        return instructorMapper.findById(instructorID);
    }

    @Override
    public List<Instructor> findAll() {
        return instructorMapper.findAll();
    }

    @Override
    public void insertInstructor(Instructor instructor) {
        instructorMapper.insertInstructor(instructor);
    }

    @Override
    public void updateInstructor(Instructor instructor) {
        instructorMapper.updateInstructor(instructor);
    }

    @Override
    public void deleteInstructor(Integer instructorID) {
        instructorMapper.deleteInstructor(instructorID);
    }
}
