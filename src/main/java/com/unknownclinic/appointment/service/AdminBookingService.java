package com.unknownclinic.appointment.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unknownclinic.appointment.dto.AdminBookingView;
import com.unknownclinic.appointment.repository.BookingMapper;

@Service
public class AdminBookingService {

	@Autowired
	private BookingMapper bookingMapper;

	/**
	 * 指定日の時間枠別予約状況を取得
	 * 全時間枠を表示し、予約がない枠は空きとして表示
	 */
	public List<AdminBookingView> getTimeSlotBookingsByDate(LocalDate date) {
		return bookingMapper.findAdminTimeSlotBookingsByDate(date);
	}

	/**
	 * 営業形態を考慮した指定日の時間枠別予約状況を取得
	 */
	public List<AdminBookingView> getTimeSlotBookingsByDateAndBusinessType(
			LocalDate date, String businessType) {
		if (businessType == null) {
			businessType = "allday"; // デフォルト値
		}
		return bookingMapper.findAdminTimeSlotBookingsByDateAndBusinessType(
				date, businessType);
	}

	/**
	 * 指定日の予約一覧を取得（予約がある分のみ）
	 */
	public List<AdminBookingView> getBookingsByDate(LocalDate date) {
		return bookingMapper.findAdminBookingsByDate(date);
	}
}