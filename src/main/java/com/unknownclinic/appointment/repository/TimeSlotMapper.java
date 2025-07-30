package com.unknownclinic.appointment.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.unknownclinic.appointment.domain.TimeSlot;

@Mapper
public interface TimeSlotMapper {

	List<TimeSlot> findByBusinessDayId(
			@Param("businessDayId") Long businessDayId);

	void insert(TimeSlot timeSlot);

	void update(TimeSlot timeSlot);

	void deleteByBusinessDayId(@Param("businessDayId") Long businessDayId);
}