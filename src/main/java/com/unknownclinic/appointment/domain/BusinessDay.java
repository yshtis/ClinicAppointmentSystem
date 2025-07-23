package com.unknownclinic.appointment.domain;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import lombok.Data;


@Data
public class BusinessDay {

	@NotNull
	private LocalDate date;
	
	@NotNull
	private boolean isOpen;
}
