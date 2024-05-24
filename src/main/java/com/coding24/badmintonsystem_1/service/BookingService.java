package com.coding24.badmintonsystem_1.service;

import com.coding24.badmintonsystem_1.entity.Booking;

import java.util.List;

public interface BookingService {
    Booking findById(Integer bookingID);
    List<Booking> findAll();
    void insertBooking(Booking booking);
    void updateBooking(Booking booking);
    void deleteBooking(Integer bookingID);
    boolean isBookingConflicting(Booking booking); // New method to check booking conflicts

    void deleteExpiredBookings();
}

