package com.unknownclinic.appointment.service;

import java.time.LocalTime;
import java.util.List;

import com.unknownclinic.appointment.domain.TimeSlot;
import com.unknownclinic.appointment.dto.TimeSlotView;

public interface TimeSlotService {

	List<TimeSlot> getAllTimeSlots();

	List<TimeSlot> getActiveTimeSlots();

	TimeSlot getTimeSlotById(Long id);

	TimeSlot getTimeSlotByStartTime(LocalTime startTime);

	List<TimeSlotView> getTimeSlotViewsForDisplay();

	void createTimeSlot(LocalTime startTime, LocalTime endTime);

	void updateTimeSlot(Long id, LocalTime startTime, LocalTime endTime,
			Boolean isActive);

	void deleteTimeSlot(Long id);
}