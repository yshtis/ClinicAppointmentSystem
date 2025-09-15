package com.unknownclinic.appointment.service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unknownclinic.appointment.domain.TimeSlot;
import com.unknownclinic.appointment.dto.TimeSlotView;
import com.unknownclinic.appointment.repository.TimeSlotMapper;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {

	@Autowired
	private TimeSlotMapper timeSlotMapper;

	@Override
	public List<TimeSlot> getAllTimeSlots() {
		return timeSlotMapper.findAll();
	}

	@Override
	public List<TimeSlot> getActiveTimeSlots() {
		return timeSlotMapper.findAllActive();
	}

	@Override
	public TimeSlot getTimeSlotById(Long id) {
		return timeSlotMapper.findById(id);
	}

	@Override
	public TimeSlot getTimeSlotByStartTime(LocalTime startTime) {
		return timeSlotMapper.findByStartTime(startTime);
	}

	@Override
	public List<TimeSlotView> getTimeSlotViewsForDisplay() {
		return getActiveTimeSlots().stream()
				.map(TimeSlotView::new)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void createTimeSlot(LocalTime startTime, LocalTime endTime) {
		TimeSlot existing = timeSlotMapper.findByStartTime(startTime);
		if (existing != null) {
			throw new IllegalStateException("同じ開始時間の時間枠が既に存在します。");
		}

		TimeSlot timeSlot = new TimeSlot();
		timeSlot.setStartTime(startTime);
		timeSlot.setEndTime(endTime);
		timeSlot.setIsActive(true);
		timeSlotMapper.insert(timeSlot);
	}

	@Override
	@Transactional
	public void updateTimeSlot(Long id, LocalTime startTime, LocalTime endTime,
			Boolean isActive) {
		TimeSlot timeSlot = timeSlotMapper.findById(id);
		if (timeSlot == null) {
			throw new IllegalArgumentException("指定された時間枠が存在しません。");
		}

		timeSlot.setStartTime(startTime);
		timeSlot.setEndTime(endTime);
		timeSlot.setIsActive(isActive);
		timeSlotMapper.update(timeSlot);
	}

	@Override
	@Transactional
	public void deleteTimeSlot(Long id) {
		TimeSlot timeSlot = timeSlotMapper.findById(id);
		if (timeSlot == null) {
			throw new IllegalArgumentException("指定された時間枠が存在しません。");
		}
		timeSlotMapper.delete(id);
	}
}