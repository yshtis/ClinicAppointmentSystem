package com.unknownclinic.appointment.domain;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class Booking {
	
	private Long id;
	
	@NotNull
	private Long userId;
	
	@NotNull
	private LocalDate date;
	
	@NotNull
	private Long timeSlotId;
	/*
	   idに割り当てる時間枠
	   1	09:00-09:30
	   2	09:30-10:00
	   3	10:00-10:30
	   4	10:30-11:00
	   5	11:00-11:30
	   6	11:30-12:00
	   7	13:00-13:30
	   8	13:30-14:00
	   9	14:00-14:30
	   10	14:30-15:00
	   11	15:00-15:30
	   12	15:30-16:00
	   13	16:00-16:30
	   14	16:30-17:00
	*/
	
	@NotNull
	private String status; // reservedまたはcancelledが格納
}