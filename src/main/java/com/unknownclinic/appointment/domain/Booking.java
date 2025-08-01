package com.unknownclinic.appointment.domain;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class Booking {
	
	private Long id;
	
	@NotNull
	private Long userId;
	
	@NotNull
	private LocalDate date;
	
	@NotNull
	private Long timeSlotId;
	
	@NotNull
	private String status; // reservedまたはcancelledが格納
}