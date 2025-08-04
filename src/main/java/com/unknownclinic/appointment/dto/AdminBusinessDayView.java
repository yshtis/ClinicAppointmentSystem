package com.unknownclinic.appointment.dto;

import lombok.Data;

@Data
public class AdminBusinessDayView {

	private Long id;
	private String businessDate; // yyyy-MM-dd
	private String type; // "allday", "am", "pm"
}