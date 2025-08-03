package com.unknownclinic.appointment.service;

import java.util.List;
import java.util.Set;

import com.unknownclinic.appointment.domain.BusinessDay;
import com.unknownclinic.appointment.dto.BookingView;
import com.unknownclinic.appointment.dto.TimeSlotView;

public interface BookingService {

	/**
	 * 営業日一覧を取得
	 */
	List<BusinessDay> getBusinessDays();

	/**
	 * 指定営業日IDで営業日を取得
	 */
	BusinessDay getBusinessDayById(Long businessDayId);

	/**
	 * 指定営業日の予約可能枠一覧（画面表示用DTO）を返す
	 */
	List<TimeSlotView> getTimeSlotsForView(Long businessDayId);

	/**
	 * 指定営業日の予約済み枠IDセット（businessDaySlotId）を返す
	 */
	Set<Long> getBookedSlotIdsForBusinessDay(Long businessDayId);

	/**
	 * 枠IDで画面表示用DTOを返す
	 */
	TimeSlotView getTimeSlotViewByBusinessDaySlotId(Long businessDaySlotId);

	/**
	 * 予約登録。二重予約などの場合は例外スロー
	 */
	void createBooking(Long userId, Long businessDaySlotId);

	/**
	 * マイページ用：ユーザーの全予約（画面表示用DTO/ラベル・日付付き）
	 */
	List<BookingView> getBookingViewsByUser(Long userId);

	/**
	 * 予約キャンセル（本人確認あり）
	 */
	void cancelBooking(Long bookingId, Long userId);
}