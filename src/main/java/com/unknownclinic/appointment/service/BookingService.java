package com.unknownclinic.appointment.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.unknownclinic.appointment.domain.Booking;
import com.unknownclinic.appointment.domain.BusinessDay;
import com.unknownclinic.appointment.dto.BookingView;
import com.unknownclinic.appointment.dto.TimeSlotView;

public interface BookingService {

	List<BusinessDay> getBusinessDays();

	BusinessDay getBusinessDayById(Long businessDayId);

	List<TimeSlotView> getTimeSlotsForView(LocalDate businessDate);

	Set<Long> getBookedSlotIdsForBusinessDate(LocalDate businessDate);

	TimeSlotView getTimeSlotViewBySlotId(Long timeSlotId,
			LocalDate businessDate);

	void createBooking(Long userId, LocalDate businessDate, Long timeSlotId);

	List<BookingView> getBookingViewsByUser(Long userId);

	void cancelBooking(Long bookingId, Long userId);

	Booking findReservedBySlot(LocalDate businessDate, Long timeSlotId);
}