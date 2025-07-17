package com.unknownclinic.appointment.domain;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public class Booking {
	
	private Long id;
	
	@NotNull
	private Long userId;
	
	@NotNull
	private LocalDate date;
	
	@NotNull
	private Long timeSlotId;

	@NotNull
	private String status;
}