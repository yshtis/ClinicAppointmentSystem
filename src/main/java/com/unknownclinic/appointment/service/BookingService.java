package com.unknownclinic.appointment.service;

import java.util.List;
import java.util.Map;

import com.unknownclinic.appointment.domain.Booking;
import com.unknownclinic.appointment.domain.BusinessDay;
import com.unknownclinic.appointment.domain.TimeSlot;

public interface BookingService {

    List<BusinessDay> getBusinessDays();

    Map<Long, List<TimeSlot>> getAllTimeSlotsGroupedByBusinessDay();

    Map<Long, Booking> getBookingsForBusinessDays(List<BusinessDay> businessDays);

    BusinessDay getBusinessDayById(Long businessDayId);

    TimeSlot getTimeSlotById(Long timeSlotId);

    void createBooking(Long userId, Long businessDayId, Long timeSlotId);

    Map<Long, Booking> getUserBookingsForBusinessDays(Long userId, List<BusinessDay> businessDays);
}