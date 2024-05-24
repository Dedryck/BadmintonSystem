package com.coding24.badmintonsystem_1.controller;

import com.coding24.badmintonsystem_1.entity.Booking;
import com.coding24.badmintonsystem_1.entity.Court;
import com.coding24.badmintonsystem_1.service.BookingService;
import com.coding24.badmintonsystem_1.service.CourtService;
import com.coding24.badmintonsystem_1.service.UserService;
import com.coding24.badmintonsystem_1.viewmodel.CourtBookingViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
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


    @GetMapping("/court-bookings")
    public String getCourtBookings(Model model, @RequestParam(value = "message", required = false) String message) {
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

        // 获取当前用户信息
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("role", userDetails.getAuthorities().iterator().next().getAuthority());

        }

        if (message != null) {
            model.addAttribute("message", message);
        }

        model.addAttribute("courtBookingViewModels", courtBookingViewModels);
        model.addAttribute("newBooking", new Booking());
        model.addAttribute("courts", courts);
        model.addAttribute("activePage", "courtBookings");
        return "court-bookings";
    }

    @PostMapping("/add-booking")
    public String addBooking(Booking booking, RedirectAttributes redirectAttributes,
                             @RequestParam("username") String username,
                             @RequestParam("redirectPage") String redirectPage) {
        System.out.println("Received booking data: " + booking);
        System.out.println("Received username: " + username);

        Integer userID = userService.getUserIDByUsername(username);
        booking.setUserID(userID);
        booking.setBookingDate(new Date());

        if (!bookingService.isBookingConflicting(booking)) {
            bookingService.insertBooking(booking);
            redirectAttributes.addAttribute("message", "预订成功");
        } else {
            redirectAttributes.addAttribute("error", "Booking conflict detected!");
        }

        redirectAttributes.addAttribute("activePage", redirectPage);

        if ("courtBookingManagement".equals(redirectPage)) {
            return "redirect:/court-booking-management";
        } else {
            return "redirect:/court-bookings";
        }
    }



    @GetMapping("/court-booking-management")
    public String getCourtBookingManagement(Model model, @RequestParam(value = "message", required = false) String message) {
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

        // 获取当前用户信息
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("role", userDetails.getAuthorities().iterator().next().getAuthority());

        }

        if (message != null) {
            model.addAttribute("message", message);
        }

        model.addAttribute("courtBookingViewModels", courtBookingViewModels);
        model.addAttribute("newBooking", new Booking());
        model.addAttribute("courts", courts);
        model.addAttribute("activePage", "courtBookingManagement");
        return "court-booking-management";
    }



    @PostMapping("/edit-booking")
    public String editBooking(Booking booking, RedirectAttributes redirectAttributes, @RequestParam("username") String username) {
        // 使用UserService的getUserIDByUsername方法获取用户ID
        Integer userID = userService.getUserIDByUsername(username);
        booking.setUserID(userID);

        // 使用StartTime的日期部分设置BookingDate
        if (booking.getStartTime() != null) {
            booking.setBookingDate(new Date(booking.getStartTime().getTime()));
        }

        if (!bookingService.isBookingConflicting(booking)) {
            bookingService.updateBooking(booking);
            redirectAttributes.addAttribute("message", "预订修改成功");
        } else {
            redirectAttributes.addAttribute("error", "Booking conflict detected!");
        }

        return "redirect:/court-booking-management";
    }


    @GetMapping("/delete-booking")
    public String deleteBooking(@RequestParam("bookingID") Integer bookingID, RedirectAttributes redirectAttributes) {
        bookingService.deleteBooking(bookingID);
        redirectAttributes.addAttribute("message", "预订删除成功");
        redirectAttributes.addAttribute("activePage", "courtBookingManagement");

        return "redirect:/court-booking-management";
    }

}
