package com.unknownclinic.appointment.domain;

import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class TimeSlot {
	
	private long id;
	
    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;
}