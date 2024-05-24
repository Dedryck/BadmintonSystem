package com.coding24.badmintonsystem_1.viewmodel;

import com.coding24.badmintonsystem_1.entity.Booking;
import com.coding24.badmintonsystem_1.entity.Court;
import lombok.Data;

import java.util.List;

//用于显示球场预订的一个视图
@Data
public class CourtBookingViewModel {
    private Court court;
    private List<Booking> bookings;
}
