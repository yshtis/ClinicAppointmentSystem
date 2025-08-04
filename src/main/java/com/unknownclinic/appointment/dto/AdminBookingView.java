package com.unknownclinic.appointment.dto;

import lombok.Data;

@Data
public class AdminBookingView {
	
	private String patientName;
	private String phoneNumber;
	private String cardNumber;
	private Integer timeSlotId;
	private String timeLabel;
}