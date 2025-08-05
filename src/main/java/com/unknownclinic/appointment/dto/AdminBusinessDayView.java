package com.unknownclinic.appointment.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import lombok.Data;

@Data
public class AdminBusinessDayView {
	private Long id;
	private String businessDate;
	private String type;

	public String getBusinessDayLabel() {
		String typeLabel = "【不明】";
		if ("allday".equals(type))
			typeLabel = "【全日】";
		else if ("am".equals(type))
			typeLabel = "【午前】";
		else if ("pm".equals(type))
			typeLabel = "【午後】";
		try {
			LocalDate date = LocalDate.parse(businessDate);
			DateTimeFormatter fmt = DateTimeFormatter
					.ofPattern("yyyy/MM/dd (E)", Locale.JAPANESE);
			return date.format(fmt) + typeLabel;
		} catch (Exception e) {
			return businessDate + typeLabel;
		}
	}
}