package com.unknownclinic.appointment.repository;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.unknownclinic.appointment.domain.BusinessDay;

@Mapper
public interface BusinessDayMapper {
	List<BusinessDay> findByMonth(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);
}