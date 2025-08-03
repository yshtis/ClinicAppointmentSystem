package com.unknownclinic.appointment.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.unknownclinic.appointment.domain.BusinessDaySlot;

@Mapper
public interface BusinessDaySlotMapper {
	// 営業日IDで有効な枠一覧
	List<BusinessDaySlot> findAvailableByBusinessDayId(
			@Param("businessDayId") Long businessDayId);

	BusinessDaySlot findById(@Param("id") Long id);

	// 複数IDで枠取得
	List<BusinessDaySlot> findByIds(@Param("list") List<Long> ids);
}