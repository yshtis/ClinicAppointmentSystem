package com.unknownclinic.appointment.domain;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public class Reservation {
	
	private Long id;
	
	@NotNull
	private String userId;

	@NotNull
	private LocalDateTime reservedAt;

	@NotNull
	private String status;
}