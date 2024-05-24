package com.coding24.badmintonsystem_1.service;

import com.coding24.badmintonsystem_1.entity.Instructor;
import java.util.List;

public interface InstructorService {
    Instructor findById(Integer instructorID);
    List<Instructor> findAll();
    void insertInstructor(Instructor instructor);
    void updateInstructor(Instructor instructor);
    void deleteInstructor(Integer instructorID);
}
