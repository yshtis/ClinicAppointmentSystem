package com.unknownclinic.appointment.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.unknownclinic.appointment.domain.BusinessDaySlot;

@Mapper
public interface BusinessDaySlotMapper {

	List<BusinessDaySlot> findAvailableByBusinessDayId(
			@Param("businessDayId") Long businessDayId);

	BusinessDaySlot findById(@Param("id") Long id);

	List<BusinessDaySlot> findByIds(@Param("list") List<Long> ids);

	void insert(BusinessDaySlot slot);
}