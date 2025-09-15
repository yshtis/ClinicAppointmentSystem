package com.unknownclinic.appointment.service;

import java.time.LocalDate;
import java.util.List;

import com.unknownclinic.appointment.dto.AdminBookingView;

public interface AdminBookingService {
    List<AdminBookingView> getTimeSlotBookingsByDate(LocalDate date);

    List<AdminBookingView> getTimeSlotBookingsByDateAndBusinessType(LocalDate date, String businessType);

    List<AdminBookingView> getBookingsByDate(LocalDate date);
}