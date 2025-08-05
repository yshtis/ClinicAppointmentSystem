package com.unknownclinic.appointment.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unknownclinic.appointment.dto.AdminBookingView;
import com.unknownclinic.appointment.repository.BookingMapper;

@Service
public class AdminBookingService {

	@Autowired
	private BookingMapper bookingMapper;

	public List<AdminBookingView> getTimeSlotBookingsByDate(LocalDate date) {
		return bookingMapper.findAdminTimeSlotBookingsByDate(date);
	}

	public List<AdminBookingView> getBookingsByDate(LocalDate date) {
		return bookingMapper.findAdminBookingsByDate(date);
	}
}