package com.unknownclinic.appointment.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.unknownclinic.appointment.dto.BookingListItem;

@Mapper
public interface BookingMapper {

	List<BookingListItem> findAllBookingsWithUserAndTime();
}
