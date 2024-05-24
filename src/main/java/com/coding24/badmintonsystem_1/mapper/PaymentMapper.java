package com.coding24.badmintonsystem_1.mapper;

import com.coding24.badmintonsystem_1.entity.Payment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PaymentMapper {

    @Select("SELECT PaymentID, BookingID, UserID, Amount, PaymentMethod, PaymentStatus, PaymentDate FROM payments WHERE PaymentID = #{paymentID}")
    @Results({
            @Result(property = "paymentID", column = "PaymentID"),
            @Result(property = "bookingID", column = "BookingID"),
            @Result(property = "userID", column = "UserID"),
            @Result(property = "amount", column = "Amount"),
            @Result(property = "paymentMethod", column = "PaymentMethod"),
            @Result(property = "paymentStatus", column = "PaymentStatus"),
            @Result(property = "paymentDate", column = "PaymentDate")
    })
    Payment findById(@Param("paymentID") Integer paymentID);

    @Select("SELECT PaymentID, BookingID, UserID, Amount, PaymentMethod, PaymentStatus, PaymentDate FROM payments")
    @Results({
            @Result(property = "paymentID", column = "PaymentID"),
            @Result(property = "bookingID", column = "BookingID"),
            @Result(property = "userID", column = "UserID"),
            @Result(property = "amount", column = "Amount"),
            @Result(property = "paymentMethod", column = "PaymentMethod"),
            @Result(property = "paymentStatus", column = "PaymentStatus"),
            @Result(property = "paymentDate", column = "PaymentDate")
    })
    List<Payment> findAll();

    @Insert("INSERT INTO payments (BookingID, UserID, Amount, PaymentMethod, PaymentStatus, PaymentDate) VALUES (#{bookingID}, #{userID}, #{amount}, #{paymentMethod}, #{paymentStatus}, #{paymentDate})")
    @Options(useGeneratedKeys = true, keyProperty = "paymentID", keyColumn = "PaymentID")
    void insertPayment(Payment payment);

    @Update("UPDATE payments SET BookingID = #{bookingID}, UserID = #{userID}, Amount = #{amount}, PaymentMethod = #{paymentMethod}, PaymentStatus = #{paymentStatus}, PaymentDate = #{paymentDate} WHERE PaymentID = #{paymentID}")
    void updatePayment(Payment payment);

    @Delete("DELETE FROM payments WHERE PaymentID = #{paymentID}")
    void deletePayment(@Param("paymentID") Integer paymentID);
}
