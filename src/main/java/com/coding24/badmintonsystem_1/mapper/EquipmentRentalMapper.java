package com.coding24.badmintonsystem_1.mapper;

import com.coding24.badmintonsystem_1.entity.EquipmentRental;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EquipmentRentalMapper {

    @Select("SELECT RentalID, EquipmentType, Quantity, RentalDate, ReturnDate, Username, Status, ImagePath FROM equipmentrentals WHERE RentalID = #{rentalID}")
    @Results({
            @Result(property = "rentalID", column = "RentalID"),
            @Result(property = "equipmentType", column = "EquipmentType"),
            @Result(property = "quantity", column = "Quantity"),
            @Result(property = "rentalDate", column = "RentalDate"),
            @Result(property = "returnDate", column = "ReturnDate"),
            @Result(property = "username", column = "Username"),
            @Result(property = "status", column = "Status"),
            @Result(property = "imagePath", column = "ImagePath")
    })
    EquipmentRental findById(@Param("rentalID") Integer rentalID);

    @Select("SELECT RentalID, EquipmentType, Quantity, RentalDate, ReturnDate, Username, Status, ImagePath FROM equipmentrentals")
    @Results({
            @Result(property = "rentalID", column = "RentalID"),
            @Result(property = "equipmentType", column = "EquipmentType"),
            @Result(property = "quantity", column = "Quantity"),
            @Result(property = "rentalDate", column = "RentalDate"),
            @Result(property = "returnDate", column = "ReturnDate"),
            @Result(property = "username", column = "Username"),
            @Result(property = "status", column = "Status"),
            @Result(property = "imagePath", column = "ImagePath")
    })
    List<EquipmentRental> findAll();

    @Insert("INSERT INTO equipmentrentals (EquipmentType, Quantity, RentalDate, ReturnDate, Username, Status, ImagePath) VALUES (#{equipmentType}, #{quantity}, #{rentalDate}, #{returnDate}, #{username}, #{status}, #{imagePath})")
    @Options(useGeneratedKeys = true, keyProperty = "rentalID", keyColumn = "RentalID")
    void insertEquipmentRental(EquipmentRental equipmentRental);

    @Update("UPDATE equipmentrentals SET EquipmentType = #{equipmentType}, Quantity = #{quantity}, RentalDate = #{rentalDate}, ReturnDate = #{returnDate}, Username = #{username}, Status = #{status}, ImagePath = #{imagePath} WHERE RentalID = #{rentalID}")
    void updateEquipmentRental(EquipmentRental equipmentRental);

    @Delete("DELETE FROM equipmentrentals WHERE RentalID = #{rentalID}")
    void deleteEquipmentRental(@Param("rentalID") Integer rentalID);
}
