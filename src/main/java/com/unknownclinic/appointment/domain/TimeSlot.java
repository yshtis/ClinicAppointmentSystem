package com.unknownclinic.appointment.domain;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class TimeSlot {

	private Long id;

	@NotNull
	private LocalTime startTime;

	@NotNull
	private LocalTime endTime;

	private Boolean isActive = true;

	// display_labelを動的生成
	public String getDisplayLabel() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		return startTime.format(formatter) + "-" + endTime.format(formatter);
	}
}