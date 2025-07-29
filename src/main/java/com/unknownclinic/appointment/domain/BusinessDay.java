package com.unknownclinic.appointment.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

import lombok.Data;


@Data
public class BusinessDay {

	private Long id;
	
	@NotNull
	private LocalDate businessDate;
	
	@NotNull
	private LocalDateTime createdAt;
}
