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
    private String slotType; // nullで終日営業、amまたはpmを格納
    
    @NotNull
    private boolean isAvailable;
    
    @NotNull
    private LocalDateTime createdAt;
}