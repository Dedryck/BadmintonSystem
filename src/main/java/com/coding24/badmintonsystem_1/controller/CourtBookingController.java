package com.coding24.badmintonsystem_1.controller;

import com.coding24.badmintonsystem_1.dto.ApiResponse;
import com.coding24.badmintonsystem_1.entity.Booking;
import com.coding24.badmintonsystem_1.entity.Court;
import com.coding24.badmintonsystem_1.service.BookingService;
import com.coding24.badmintonsystem_1.service.CourtService;
import com.coding24.badmintonsystem_1.service.UserService;
import com.coding24.badmintonsystem_1.viewmodel.CourtBookingViewModel;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/court-bookings")
public class CourtBookingController {

    private final BookingService bookingService;
    private final CourtService courtService;
    private final UserService userService;

    @Autowired
    public CourtBookingController(BookingService bookingService, CourtService courtService, UserService userService) {
        this.bookingService = bookingService;
        this.courtService = courtService;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MEMBER', 'ADMIN')")
    public ApiResponse<List<CourtBookingViewModel>> getCourtBookings() {
        List<Court> courts = courtService.findAll();
        List<Booking> bookings = bookingService.findAll();

        List<CourtBookingViewModel> courtBookingViewModels = courts.stream().map(court -> {
            List<Booking> courtBookings = bookings.stream()
                    .filter(booking -> booking.getCourtID().equals(court.getCourtID()))
                    .collect(Collectors.toList());
            CourtBookingViewModel viewModel = new CourtBookingViewModel();
            viewModel.setCourt(court);
            viewModel.setBookings(courtBookings);
            return viewModel;
        }).collect(Collectors.toList());

        return new ApiResponse<List<CourtBookingViewModel>>(0, "查询成功", courtBookingViewModels);
    }

    @PostMapping("/add")
    public ApiResponse<Booking> addBooking(@Valid @RequestBody Booking booking) {
        System.out.println("Received booking data: " + booking);
        // 从 token 或其他来源获取用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Integer userID = userService.getUserIDByUsername(username);
        booking.setUserID(userID);
        booking.setBookingDate(new Date());

        if (!bookingService.isBookingConflicting(booking)) {
            bookingService.insertBooking(booking);
            return new ApiResponse<>(0, "预订成功", booking);
        } else {
            return new ApiResponse<>(1, "Booking conflict detected", null);
        }
    }


    @PostMapping("/edit")
    public ApiResponse<Booking> editBooking(@Valid @RequestBody Booking booking) {
        System.out.println("Received booking data: " + booking);
        // 从当前认证的用户信息中获取用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Integer userID = userService.getUserIDByUsername(username);
        booking.setUserID(userID);

        // 设置预定日期
//        if (booking.getStartTime() != null) {
//            booking.setBookingDate(new Date(booking.getStartTime().getTime()));
//        }

        if (!bookingService.isBookingConflicting(booking)) {
            bookingService.updateBooking(booking);
            return new ApiResponse<>(0, "预订修改成功", booking);
        } else {
            return new ApiResponse<>(1, "订单冲突请重新选择时间！！！", null);
        }
    }


    @DeleteMapping("/delete/{bookingID}")
    public ApiResponse<Void> deleteBooking(@Valid @PathVariable Integer bookingID) {
        bookingService.deleteBooking(bookingID);
        return new ApiResponse<>(0, "预订删除成功", null);
    }
}
