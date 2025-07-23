package com.unknownclinic.appointment.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class BookingListItem {
	private Long bookingId;
	private LocalDate date;
	private LocalTime startTime;
	private LocalTime endTime;
	private String userName;
	private String cardNumber;
	private String status;
}