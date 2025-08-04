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
     * 指定日の全時間枠に対する予約情報（タイムテーブル形式）を返す
     * 各時間枠ごとに、患者情報（予約がなければ空）を含む
     * @param date 予約日
     * @return AdminBookingViewリスト（時間枠順）
     */
    public List<AdminBookingView> getTimeSlotBookingsByDate(LocalDate date) {
        return bookingMapper.findAdminTimeSlotBookingsByDate(date);
    }

    /**
	 * 指定日の予約情報を返す
	 * 各予約ごとに、患者情報（予約がなければ空）を含む
	 * @param date 予約日
	 * @return AdminBookingViewリスト（時間枠順）
	 */
    public List<AdminBookingView> getBookingsByDate(LocalDate date) {
        return bookingMapper.findAdminBookingsByDate(date);
    }
}