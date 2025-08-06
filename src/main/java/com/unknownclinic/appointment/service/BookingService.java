package com.unknownclinic.appointment.service;

import java.util.List;
import java.util.Set;

import com.unknownclinic.appointment.domain.Booking;
import com.unknownclinic.appointment.domain.BusinessDay;
import com.unknownclinic.appointment.dto.BookingView;
import com.unknownclinic.appointment.dto.TimeSlotView;

public interface BookingService {

	List<BusinessDay> getBusinessDays();

	BusinessDay getBusinessDayById(Long businessDayId);

	List<TimeSlotView> getTimeSlotsForView(Long businessDayId);

	Set<Long> getBookedSlotIdsForBusinessDay(Long businessDayId);

	TimeSlotView getTimeSlotViewByBusinessDaySlotId(Long businessDaySlotId);

	void createBooking(Long userId, Long businessDaySlotId);

	List<BookingView> getBookingViewsByUser(Long userId);

	void cancelBooking(Long bookingId, Long userId);
	
	Booking findReservedBySlot(Long businessDaySlotId);
}