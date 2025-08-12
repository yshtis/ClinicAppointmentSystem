package com.unknownclinic.appointment.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unknownclinic.appointment.domain.Booking;
import com.unknownclinic.appointment.domain.BusinessDay;
import com.unknownclinic.appointment.domain.TimeSlot;
import com.unknownclinic.appointment.dto.BookingView;
import com.unknownclinic.appointment.dto.TimeSlotView;
import com.unknownclinic.appointment.repository.BookingMapper;
import com.unknownclinic.appointment.repository.BusinessDayMapper;
import com.unknownclinic.appointment.repository.TimeSlotMapper;

@Service
public class BookingServiceImpl implements BookingService {

	@Autowired
	private BusinessDayMapper businessDayMapper;

	@Autowired
	private TimeSlotMapper timeSlotMapper;

	@Autowired
	private BookingMapper bookingMapper;

	@Override
	public List<BusinessDay> getBusinessDays() {
		LocalDate today = LocalDate.now();
		return businessDayMapper.findAllActive().stream()
				.filter(day -> !day.getBusinessDate().isBefore(today))
				.sorted(Comparator.comparing(BusinessDay::getBusinessDate))
				.collect(Collectors.toList());
	}

	@Override
	public BusinessDay getBusinessDayById(Long businessDayId) {
		return businessDayMapper.findById(businessDayId);
	}

	@Override
	public List<TimeSlotView> getTimeSlotsForView(LocalDate businessDate) {
		if (businessDate == null)
			return List.of();

		// アクティブな営業日かチェック
		BusinessDay businessDay = businessDayMapper.findByDate(businessDate);
		if (businessDay == null || !businessDay.getIsActive()) {
			return List.of();
		}

		List<TimeSlot> timeSlots = timeSlotMapper.findAllActive();
		return timeSlots.stream()
				.map(slot -> new TimeSlotView(slot.getId(),
						slot.getDisplayLabel(), businessDate))
				.collect(Collectors.toList());
	}

	@Override
	public Set<Long> getBookedSlotIdsForBusinessDate(LocalDate businessDate) {
		if (businessDate == null)
			return Set.of();

		List<Booking> bookings = bookingMapper.findByDate(businessDate);
		return bookings.stream()
				.filter(b -> "reserved".equalsIgnoreCase(b.getStatus()))
				.map(Booking::getTimeSlotId)
				.collect(Collectors.toSet());
	}

	@Override
	public TimeSlotView getTimeSlotViewBySlotId(Long timeSlotId,
			LocalDate businessDate) {
		TimeSlot timeSlot = timeSlotMapper.findById(timeSlotId);
		if (timeSlot == null)
			return null;

		return new TimeSlotView(timeSlot.getId(), timeSlot.getDisplayLabel(),
				businessDate);
	}

	@Override
	@Transactional
	public void createBooking(Long userId, LocalDate businessDate,
			Long timeSlotId) {
		// 営業日チェック
		BusinessDay businessDay = businessDayMapper.findByDate(businessDate);
		if (businessDay == null || !businessDay.getIsActive()) {
			throw new IllegalStateException("指定された日付は営業日ではありません。");
		}

		// 時間枠チェック
		TimeSlot timeSlot = timeSlotMapper.findById(timeSlotId);
		if (timeSlot == null || !timeSlot.getIsActive()) {
			throw new IllegalStateException("指定された時間枠は利用できません。");
		}

		// 枠が予約済みかチェック
		Booking reserved = bookingMapper.findReservedBySlot(businessDate,
				timeSlotId);
		if (reserved != null) {
			throw new IllegalStateException("この枠はすでに予約済みです。");
		}

		// 同じユーザーの同一日重複チェック
		Booking userBooking = bookingMapper
				.findReservedByUserAndBusinessDate(userId, businessDate);
		if (userBooking != null) {
			throw new IllegalStateException("同じ日に複数予約はできません。");
		}

		Booking booking = new Booking();
		booking.setUserId(userId);
		booking.setBusinessDate(businessDate);
		booking.setTimeSlotId(timeSlotId);
		booking.setStatus("reserved");
		bookingMapper.insert(booking);
	}

	@Override
	public List<BookingView> getBookingViewsByUser(Long userId) {
		List<Booking> bookings = bookingMapper.findByUserId(userId);
		List<TimeSlot> timeSlots = timeSlotMapper.findAll();

		return bookings.stream().map(booking -> {
			TimeSlot timeSlot = timeSlots.stream()
					.filter(ts -> ts.getId().equals(booking.getTimeSlotId()))
					.findFirst().orElse(null);
			String timeLabel = timeSlot != null ? timeSlot.getDisplayLabel()
					: "不明";
			return new BookingView(booking, booking.getBusinessDate(),
					timeLabel);
		}).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void cancelBooking(Long bookingId, Long userId) {
		Booking booking = bookingMapper.findById(bookingId);
		if (booking == null || !booking.getUserId().equals(userId)) {
			throw new IllegalArgumentException("予約が存在しないか、権限がありません");
		}

		// 過去の予約はキャンセルできない
		if (booking.getBusinessDate().isBefore(LocalDate.now())) {
			throw new IllegalStateException("過去の予約はキャンセルできません。");
		}

		booking.setStatus("cancelled");
		bookingMapper.update(booking);
	}

	@Override
	public Booking findReservedBySlot(LocalDate businessDate, Long timeSlotId) {
		return bookingMapper.findReservedBySlot(businessDate, timeSlotId);
	}
}