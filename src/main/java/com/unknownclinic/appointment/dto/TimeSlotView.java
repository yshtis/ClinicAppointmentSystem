package com.unknownclinic.appointment.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TimeSlotView {

	private Long id;
	private String label;
	private LocalDate businessDate;

	public TimeSlotView(Long id, String label) {
		this.id = id;
		this.label = label;
	}

	public TimeSlotView(Long id, String label, LocalDate businessDate) {
		this.id = id;
		this.label = label;
		this.businessDate = businessDate;
	}

	public TimeSlotView(
			com.unknownclinic.appointment.domain.TimeSlot timeSlot) {
		this.id = timeSlot.getId();
		this.label = timeSlot.getDisplayLabel();
	}

	public TimeSlotView(com.unknownclinic.appointment.domain.TimeSlot timeSlot,
			LocalDate businessDate) {
		this.id = timeSlot.getId();
		this.label = timeSlot.getDisplayLabel();
		this.businessDate = businessDate;
	}
}