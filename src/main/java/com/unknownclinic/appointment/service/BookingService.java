package com.unknownclinic.appointment.service;

import java.util.List;

import com.unknownclinic.appointment.domain.Booking;
import com.unknownclinic.appointment.domain.BusinessDay;
import com.unknownclinic.appointment.domain.TimeSlot;

public interface BookingService {

	List<BusinessDay> getBusinessDays();

	BusinessDay getBusinessDayById(Long businessDayId);

	TimeSlot getTimeSlotById(Long timeSlotId);

	void createBooking(Long userId, Long businessDayId, Long timeSlotId);
	
	List<Booking> getBookingsForBusinessDay(Long businessDayId);
	
	List<TimeSlot> getTimeSlotsByBusinessDayId(Long businessDayId);
	
	// mypage用
    List<Booking> getBookingsByUser(Long userId);
    
    void cancelBooking(Long bookingId, Long userId);
}