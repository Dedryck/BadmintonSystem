package com.coding24.badmintonsystem_1.mapper;

import com.coding24.badmintonsystem_1.entity.Court;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourtMapper {

    @Select("SELECT CourtID, Location, Status FROM courts WHERE CourtID = #{courtID}")
    @Results({
            @Result(property = "courtID", column = "CourtID"),
            @Result(property = "location", column = "Location"),
            @Result(property = "status", column = "Status")
    })
    Court findById(@Param("courtID") Integer courtID);

    @Select("SELECT CourtID, Location, Status FROM courts")
    @Results({
            @Result(property = "courtID", column = "CourtID"),
            @Result(property = "location", column = "Location"),
            @Result(property = "status", column = "Status")
    })
    List<Court> findAll();

    @Insert("INSERT INTO courts (Location, Status) VALUES (#{location}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "courtID", keyColumn = "CourtID")
    void insertCourt(Court court);

    @Update("UPDATE courts SET Location = #{location}, Status = #{status} WHERE CourtID = #{courtID}")
    void updateCourt(Court court);

    @Delete("DELETE FROM courts WHERE CourtID = #{courtID}")
    void deleteCourt(@Param("courtID") Integer courtID);
}
