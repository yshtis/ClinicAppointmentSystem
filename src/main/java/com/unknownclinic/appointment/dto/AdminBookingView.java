package com.unknownclinic.appointment.dto;

import lombok.Data;

@Data
public class AdminBookingView {

	private String patientName;
	private String phoneNumber;
	private String cardNumber;
	private Long timeSlotId;
	private String timeLabel;

	// 予約がない時間枠用のコンストラクタ
	public AdminBookingView() {
	}

	// 予約がある時間枠用のコンストラクタ  
	public AdminBookingView(Long timeSlotId, String timeLabel,
			String patientName, String cardNumber, String phoneNumber) {
		this.timeSlotId = timeSlotId;
		this.timeLabel = timeLabel;
		this.patientName = patientName;
		this.cardNumber = cardNumber;
		this.phoneNumber = phoneNumber;
	}

	// 空き枠用のコンストラクタ
	public AdminBookingView(Long timeSlotId, String timeLabel) {
		this.timeSlotId = timeSlotId;
		this.timeLabel = timeLabel;
		this.patientName = null;
		this.cardNumber = null;
		this.phoneNumber = null;
	}

	public boolean hasBooking() {
		return patientName != null && !patientName.trim().isEmpty();
	}

	public String getDisplayText() {
		if (hasBooking()) {
			return String.format("%s (%s)", patientName, cardNumber);
		}
		return "空き";
	}

	public String getStatusClass() {
		return hasBooking() ? "text-primary" : "text-muted";
	}
}