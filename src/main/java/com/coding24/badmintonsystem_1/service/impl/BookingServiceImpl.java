package com.coding24.badmintonsystem_1.service.impl;

import com.coding24.badmintonsystem_1.entity.Booking;
import com.coding24.badmintonsystem_1.mapper.BookingMapper;
import com.coding24.badmintonsystem_1.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingMapper bookingMapper;

    @Autowired
    public BookingServiceImpl(BookingMapper bookingMapper) {
        this.bookingMapper = bookingMapper;
    }

    @Override
    public Booking findById(Integer bookingID) {
        return bookingMapper.findById(bookingID);
    }

    @Override
    public List<Booking> findAll() {
        return bookingMapper.findAll();
    }

    @Override
    public void insertBooking(Booking booking) {
        bookingMapper.insertBooking(booking);
    }

    @Override
    public void updateBooking(Booking booking) {
        bookingMapper.updateBooking(booking);
    }

    @Override
    public void deleteBooking(Integer bookingID) {
        bookingMapper.deleteBooking(bookingID);
    }


    @Override
    public boolean isBookingConflicting(Booking booking) {
        List<Booking> existingBookings = bookingMapper.findByCourtIdAndDate(booking.getCourtID(), booking.getStartTime(), booking.getEndTime());
        for (Booking existingBooking : existingBookings) {
            if (booking.getStartTime().before(existingBooking.getEndTime()) && booking.getEndTime().after(existingBooking.getStartTime())) {
                return true;
            }
        }
        return false;
    }
    @Override
    public void deleteExpiredBookings() {
        bookingMapper.deleteExpiredBookings();
    }


}
