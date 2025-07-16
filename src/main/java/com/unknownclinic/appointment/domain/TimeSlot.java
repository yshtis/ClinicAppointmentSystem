package com.unknownclinic.appointment.domain;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public class TimeSlot {
	
    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    @NotNull
    private Boolean isReserved;
}