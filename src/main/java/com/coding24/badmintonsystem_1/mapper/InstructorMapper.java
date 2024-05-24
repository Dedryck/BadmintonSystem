package com.coding24.badmintonsystem_1.mapper;

import com.coding24.badmintonsystem_1.entity.Instructor;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface InstructorMapper {

    @Select("SELECT * FROM instructors WHERE InstructorID = #{instructorID}")
    Instructor findById(@Param("instructorID") Integer instructorID);

    @Select("SELECT * FROM instructors")
    List<Instructor> findAll();

    @Insert("INSERT INTO instructors (Name, Phone, Email, Specialization, Available, photoPath) " +
            "VALUES (#{name}, #{phone}, #{email}, #{specialization}, #{available}, #{photoPath})")
    @Options(useGeneratedKeys = true, keyProperty = "instructorID", keyColumn = "InstructorID")
    void insertInstructor(Instructor instructor);

    @Update("UPDATE instructors SET Name = #{name}, Phone = #{phone}, Email = #{email}, Specialization = #{specialization}, " +
            "Available = #{available}, photoPath = #{photoPath} WHERE InstructorID = #{instructorID}")
    void updateInstructor(Instructor instructor);

    @Delete("DELETE FROM instructors WHERE InstructorID = #{instructorID}")
    void deleteInstructor(@Param("instructorID") Integer instructorID);
}
