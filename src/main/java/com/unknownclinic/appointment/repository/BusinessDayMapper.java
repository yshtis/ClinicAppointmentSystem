package com.unknownclinic.appointment.repository;

import java.time.YearMonth;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.unknownclinic.appointment.domain.BusinessDay;

@Mapper
public interface BusinessDayMapper {

	List<BusinessDay> findAll();

	List<BusinessDay> findByMonth(@Param("month") YearMonth month);

	void insert(BusinessDay day);

	void update(BusinessDay day);
}