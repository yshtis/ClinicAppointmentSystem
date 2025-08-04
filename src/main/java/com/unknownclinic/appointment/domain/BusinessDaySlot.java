package com.unknownclinic.appointment.domain;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class BusinessDaySlot {
	
	private Long id;

	@NotNull
	private Long businessDayId;

	@NotNull
	private Long timeSlotMasterId;

	@NotNull
	private boolean available;
}