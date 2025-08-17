package com.unknownclinic.appointment.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class Booking {

	private Long id;

	@NotNull
	private Long userId;

	@NotNull
	private LocalDate businessDate;
	
	@NotNull
	private Long timeSlotId;

	@NotNull
	private String status;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}