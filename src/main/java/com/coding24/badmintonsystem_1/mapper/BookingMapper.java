package com.coding24.badmintonsystem_1.mapper;

import com.coding24.badmintonsystem_1.entity.Booking;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface BookingMapper {

    @Select("SELECT BookingID, UserID, CourtID, StartTime, EndTime, BookingDate, Status FROM bookings WHERE BookingID = #{bookingID}")
    @Results({
            @Result(property = "bookingID", column = "BookingID"),
            @Result(property = "userID", column = "UserID"),
            @Result(property = "courtID", column = "CourtID"),
            @Result(property = "startTime", column = "StartTime"),
            @Result(property = "endTime", column = "EndTime"),
            @Result(property = "bookingDate", column = "BookingDate"),
            @Result(property = "status", column = "Status")
    })
    Booking findById(@Param("bookingID") Integer bookingID);

    @Select("SELECT BookingID, UserID, CourtID, StartTime, EndTime, BookingDate, Status FROM bookings")
    @Results({
            @Result(property = "bookingID", column = "BookingID"),
            @Result(property = "userID", column = "UserID"),
            @Result(property = "courtID", column = "CourtID"),
            @Result(property = "startTime", column = "StartTime"),
            @Result(property = "endTime", column = "EndTime"),
            @Result(property = "bookingDate", column = "BookingDate"),
            @Result(property = "status", column = "Status")
    })
    List<Booking> findAll();

    @Insert("INSERT INTO bookings (UserID, CourtID, StartTime, EndTime, BookingDate, Status) VALUES (#{userID}, #{courtID}, #{startTime}, #{endTime}, #{bookingDate}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "bookingID", keyColumn = "BookingID")
    void insertBooking(Booking booking);

    @Update("UPDATE bookings SET UserID = #{userID}, CourtID = #{courtID}, StartTime = #{startTime}, EndTime = #{endTime}, BookingDate = #{bookingDate}, Status = #{status} WHERE BookingID = #{bookingID}")
    void updateBooking(Booking booking);

    @Delete("DELETE FROM bookings WHERE BookingID = #{bookingID}")
    void deleteBooking(@Param("bookingID") Integer bookingID);

    @Select("SELECT * FROM bookings WHERE CourtID = #{courtID} AND (StartTime < #{endTime} AND EndTime > #{startTime})")
    @Results({
            @Result(property = "bookingID", column = "BookingID"),
            @Result(property = "userID", column = "UserID"),
            @Result(property = "courtID", column = "CourtID"),
            @Result(property = "startTime", column = "StartTime"),
            @Result(property = "endTime", column = "EndTime"),
            @Result(property = "bookingDate", column = "BookingDate"),
            @Result(property = "status", column = "Status")
    })
    List<Booking> findByCourtIdAndDate(@Param("courtID") Integer courtID, @Param("startTime") Date startTime, @Param("endTime") Date endTime);


    @Delete("DELETE FROM bookings WHERE EndTime < NOW() - INTERVAL 2 HOUR")
    void deleteExpiredBookings();


}
