package com.unknownclinic.appointment.domain;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class Booking {
	private Long id;

	@NotNull
	private Long userId;

	@NotNull
	private Long businessDaySlotId;

	@NotNull
	private String status; // "reserved"または"canceled"を格納

	private LocalDateTime createdAt;
}