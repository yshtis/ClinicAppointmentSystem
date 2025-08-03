package com.unknownclinic.appointment.domain;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class TimeSlotMaster {
	
	private Long id;

	@NotNull
	private String label; // "09:00-09:30" など

	@NotNull
	private java.time.LocalTime startTime;

	@NotNull
	private java.time.LocalTime endTime;

	@NotNull
	private String amPm; // "am" or "pm"
}