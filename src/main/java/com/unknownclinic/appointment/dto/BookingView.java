package com.unknownclinic.appointment.dto;

import java.time.LocalDate;

import com.unknownclinic.appointment.domain.Booking;

import lombok.Data;

@Data
public class BookingView {
	private Long id;
	private LocalDate businessDate;
	private String slotLabel;
	private String status;

	public BookingView(Booking booking, LocalDate businessDate,
			String slotLabel) {
		this.id = booking.getId();
		this.businessDate = businessDate;
		this.slotLabel = slotLabel;
		this.status = booking.getStatus();
	}

	public boolean isActiveFutureBooking() {
		return businessDate != null
				&& !businessDate.isBefore(LocalDate.now())
				&& !"cancelled".equalsIgnoreCase(status);
	}
}