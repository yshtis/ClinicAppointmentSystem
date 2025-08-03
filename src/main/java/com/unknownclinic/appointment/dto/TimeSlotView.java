package com.unknownclinic.appointment.dto;

public class TimeSlotView {

	private Long number; // 1～14の枠番号
	private String label; // 例："09:00-09:30"

	public TimeSlotView(Long number, String label) {
		this.number = number;
		this.label = label;
	}

	public Long getNumber() {
		return number;
	}

	public String getLabel() {
		return label;
	}
}
