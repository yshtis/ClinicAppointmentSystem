package com.unknownclinic.appointment.repository;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.unknownclinic.appointment.domain.Booking;
import com.unknownclinic.appointment.dto.AdminBookingView;

@Mapper
public interface BookingMapper {

	List<Booking> findByUserId(@Param("userId") Long userId);

	Booking findById(@Param("id") Long id);

	void insert(Booking booking);

	void update(Booking booking);

	List<Booking> findByDate(@Param("date") LocalDate date);

	List<Booking> findByBusinessDaySlotIds(
			@Param("list") List<Long> businessDaySlotIds);

	Booking findReservedByUserAndSlot(@Param("userId") Long userId,
			@Param("businessDaySlotId") Long businessDaySlotId);

	// 管理者画面用（日付指定で患者情報も取得）
	List<AdminBookingView> findAdminBookingsByDate(
			@Param("date") LocalDate date);

	// 管理者タイムテーブル用：全時間枠＋各枠ごとの予約情報（予約がなければnull）
	List<AdminBookingView> findAdminTimeSlotBookingsByDate(
			@Param("date") LocalDate date);
}