package com.coding24.badmintonsystem_1.scheduler;

import com.coding24.badmintonsystem_1.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BookingScheduler {

    private final BookingService bookingService;

    @Autowired
    public BookingScheduler(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Scheduled(fixedRate = 3600000) // 每小时执行一次60,000、3600000
    public void deleteExpiredBookings() {
        bookingService.deleteExpiredBookings();
    }
}
