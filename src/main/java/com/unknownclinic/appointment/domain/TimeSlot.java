package com.unknownclinic.appointment.domain;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class TimeSlot {
	
	private long id;
	
    @NotNull
    private long businessDayId;
    
    @NotNull
    private Integer slotType; // 0: 午前, 1: 午後
    
    @NotNull
    private boolean isAvailable;
    
    @NotNull
    private LocalDateTime endTime;
}