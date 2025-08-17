package com.unknownclinic.appointment.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class TimeSlotView {

	private Long id;
	private String label;
	private LocalDate businessDate;
	private LocalTime startTime;
	private LocalTime endTime;

	public TimeSlotView(Long id, String label) {
		this.id = id;
		this.label = label;
	}

	public TimeSlotView(Long id, String label, LocalDate businessDate) {
		this.id = id;
		this.label = label;
		this.businessDate = businessDate;
	}

	public TimeSlotView(
			com.unknownclinic.appointment.domain.TimeSlot timeSlot) {
		this.id = timeSlot.getId();
		this.label = timeSlot.getDisplayLabel();
		this.startTime = timeSlot.getStartTime();
		this.endTime = timeSlot.getEndTime();
	}

	public TimeSlotView(com.unknownclinic.appointment.domain.TimeSlot timeSlot,
			LocalDate businessDate) {
		this.id = timeSlot.getId();
		this.label = timeSlot.getDisplayLabel();
		this.businessDate = businessDate;
		this.startTime = timeSlot.getStartTime();
		this.endTime = timeSlot.getEndTime();
	}

	public TimeSlotView(Long id, LocalTime startTime, LocalTime endTime,
			LocalDate businessDate) {
		this.id = id;
		this.startTime = startTime;
		this.endTime = endTime;
		this.businessDate = businessDate;
		this.label = String.format("%02d:%02d-%02d:%02d",
				startTime.getHour(), startTime.getMinute(),
				endTime.getHour(), endTime.getMinute());
	}

	/**
	 * 営業形態に対してこの時間枠が利用可能かどうかを判定
	 * @param businessType 営業形態（allday/am/pm）
	 * @return 利用可能な場合true
	 */
	public boolean isAvailableForBusinessType(String businessType) {
		if (startTime == null || businessType == null) {
			return true;
		}

		switch (businessType) {
		case "am":
			return startTime.getHour() < 12;
		case "pm":
			return startTime.getHour() >= 12;
		case "allday":
		default:
			return true;
		}
	}

	/**
	 * この時間枠が午前か午後かを判定
	 * @return "午前" または "午後"
	 */
	public String getTimePeriod() {
		if (startTime == null) {
			return "不明";
		}
		return startTime.getHour() < 12 ? "午前" : "午後";
	}

	/**
	 * 営業形態に応じたCSSクラスを返す
	 * @param businessType 営業形態
	 * @return CSSクラス名
	 */
	public String getAvailabilityCssClass(String businessType) {
		if (!isAvailableForBusinessType(businessType)) {
			return "time-slot-unavailable";
		}
		return "time-slot-available";
	}

	/**
	 * 営業形態に応じた表示テキストを返す
	 * @param businessType 営業形態
	 * @return 表示用テキスト
	 */
	public String getAvailabilityText(String businessType) {
		if (!isAvailableForBusinessType(businessType)) {
			return "営業時間外";
		}
		return label;
	}

	/**
	 * デバッグ用の詳細情報
	 */
	public String getDebugInfo() {
		return String.format(
				"TimeSlotView[id=%d, label=%s, startTime=%s, businessDate=%s, period=%s]",
				id, label, startTime, businessDate, getTimePeriod());
	}

	/**
	 * 時間の範囲チェック（営業時間判定用）
	 * @param hour 判定する時間
	 * @return この時間枠に含まれる場合true
	 */
	public boolean containsHour(int hour) {
		if (startTime == null || endTime == null) {
			return false;
		}
		return hour >= startTime.getHour() && hour < endTime.getHour();
	}

	/**
	 * 営業形態の制約を考慮したラベル生成
	 * @param businessType 営業形態
	 * @return 制約を考慮したラベル
	 */
	public String getLabelWithBusinessTypeConstraint(String businessType) {
		if (isAvailableForBusinessType(businessType)) {
			return label;
		}
		return label + "（営業時間外）";
	}
}