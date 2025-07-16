package com.unknownclinic.appointment.domain;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class User {

	@NotNull
	private String id;
	
	@NotNull
	private String name;
	
	@NotNull
	private LocalDate birthDate;
}
