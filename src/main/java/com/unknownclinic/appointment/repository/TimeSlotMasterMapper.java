package com.unknownclinic.appointment.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.unknownclinic.appointment.domain.TimeSlotMaster;

@Mapper
public interface TimeSlotMasterMapper {

	List<TimeSlotMaster> findAll();

	TimeSlotMaster findById(@Param("id") Long id);

	List<TimeSlotMaster> findByAmPm(@Param("amPm") String amPm);
}