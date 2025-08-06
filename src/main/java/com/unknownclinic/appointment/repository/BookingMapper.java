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

	Booking findReservedBySlot(
			@Param("businessDaySlotId") Long businessDaySlotId);

	List<AdminBookingView> findAdminBookingsByDate(
			@Param("date") LocalDate date);

	List<AdminBookingView> findAdminTimeSlotBookingsByDate(
			@Param("date") LocalDate date);
}