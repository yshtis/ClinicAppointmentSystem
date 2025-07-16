package com.unknownclinic.appointment.domain;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class Admin {

	private Long id;
	
	@NotNull
	private String userId;

	@NotNull
	private String password;
}