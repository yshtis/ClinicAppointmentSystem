package com.unknownclinic.appointment.repository;

import java.time.LocalTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.unknownclinic.appointment.domain.TimeSlot;

@Mapper
public interface TimeSlotMapper {

	List<TimeSlot> findAll();

	List<TimeSlot> findAllActive();

	TimeSlot findById(@Param("id") Long id);

	TimeSlot findByStartTime(@Param("startTime") LocalTime startTime);

	void insert(TimeSlot timeSlot);

	void update(TimeSlot timeSlot);

	void delete(@Param("id") Long id);
}