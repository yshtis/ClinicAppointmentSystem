package com.unknownclinic.appointment.repository;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.unknownclinic.appointment.domain.BusinessDay;

@Mapper
public interface BusinessDayMapper {

	List<BusinessDay> findAll();

	List<BusinessDay> findAllActive();

	BusinessDay findById(@Param("id") Long id);

	BusinessDay findByDate(@Param("businessDate") LocalDate businessDate);

	void insert(BusinessDay day);

	void update(BusinessDay day);

	void deleteByBusinessDate(@Param("businessDate") LocalDate businessDate);

	void deleteById(@Param("id") Long id);

	int countByBusinessDate(@Param("businessDate") LocalDate businessDate);
}