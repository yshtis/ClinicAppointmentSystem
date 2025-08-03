package com.unknownclinic.appointment.repository;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.unknownclinic.appointment.domain.Booking;

@Mapper
public interface BookingMapper {

	List<Booking> findByUserId(@Param("userId") Long userId);

	Booking findById(@Param("id") Long id);

	void insert(Booking booking);

	void update(Booking booking);

	List<Booking> findByDate(@Param("date") LocalDate date);

	// 新スキーマ：business_day_slot_idで複数取得
	List<Booking> findByBusinessDaySlotIds(
			@Param("list") List<Long> businessDaySlotIds);

	// 新スキーマ：ユーザーと枠で予約取得（予約済みのみ）
	Booking findReservedByUserAndSlot(@Param("userId") Long userId,
			@Param("businessDaySlotId") Long businessDaySlotId);
}