package com.unknownclinic.appointment.domain;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

// 営業日に必要なオブジェクト、まだ実装しないが、将来のために定義しておく

@Data
public class BusinessDay {

	@NotNull
	private LocalDate date;
	
	@NotNull
	private boolean isOpen;
}
