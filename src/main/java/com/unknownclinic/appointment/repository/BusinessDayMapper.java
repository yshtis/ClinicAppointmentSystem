package com.unknownclinic.appointment.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.unknownclinic.appointment.domain.BusinessDay;

@Mapper
public interface BusinessDayMapper {
	List<BusinessDay> findAll();

	BusinessDay findById(@Param("id") Long id);

	void insert(BusinessDay day);

	void update(BusinessDay day);
}