package com.unknownclinic.appointment.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unknownclinic.appointment.dto.AdminBookingView;
import com.unknownclinic.appointment.repository.BookingMapper;

@Service
public class AdminBookingServiceImpl implements AdminBookingService {

	@Autowired
	private BookingMapper bookingMapper;

	@Override
	public List<AdminBookingView> getTimeSlotBookingsByDate(LocalDate date) {
		return bookingMapper.findAdminTimeSlotBookingsByDate(date);
	}

	@Override
	public List<AdminBookingView> getTimeSlotBookingsByDateAndBusinessType(
			LocalDate date, String businessType) {
		if (businessType == null) {
			businessType = "allday";
		}
		return bookingMapper.findAdminTimeSlotBookingsByDateAndBusinessType(
				date, businessType);
	}

	@Override
	public List<AdminBookingView> getBookingsByDate(LocalDate date) {
		return bookingMapper.findAdminBookingsByDate(date);
	}
}