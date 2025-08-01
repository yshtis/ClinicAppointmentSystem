package com.unknownclinic.appointment.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unknownclinic.appointment.domain.Booking;
import com.unknownclinic.appointment.domain.BusinessDay;
import com.unknownclinic.appointment.domain.TimeSlot;
import com.unknownclinic.appointment.repository.BookingMapper;
import com.unknownclinic.appointment.repository.BusinessDayMapper;
import com.unknownclinic.appointment.repository.TimeSlotMapper;

@Service
public class BookingServiceImpl implements BookingService {

	public static final Map<Long, String> SLOT_TIME_LABELS = Map.ofEntries(
			Map.entry(1L, "09:00-09:30"),
			Map.entry(2L, "09:30-10:00"),
			Map.entry(3L, "10:00-10:30"),
			Map.entry(4L, "10:30-11:00"),
			Map.entry(5L, "11:00-11:30"),
			Map.entry(6L, "11:30-12:00"),
			Map.entry(7L, "13:00-13:30"),
			Map.entry(8L, "13:30-14:00"),
			Map.entry(9L, "14:00-14:30"),
			Map.entry(10L, "14:30-15:00"),
			Map.entry(11L, "15:00-15:30"),
			Map.entry(12L, "15:30-16:00"),
			Map.entry(13L, "16:00-16:30"),
			Map.entry(14L, "16:30-17:00"));

	@Autowired
	private BusinessDayMapper businessDayRepository;

	@Autowired
	private TimeSlotMapper timeSlotMapper;

	@Autowired
	private BookingMapper bookingMapper;

	@Override
	public List<BusinessDay> getBusinessDays() {
		return businessDayRepository.findAll();
	}

	private Long getBusinessDayIdByDate(List<BusinessDay> businessDays,
			LocalDate date) {
		return businessDays.stream()
				.filter(bd -> bd.getBusinessDate().equals(date))
				.map(BusinessDay::getId)
				.findFirst()
				.orElse(null);
	}

	@Override
	public BusinessDay getBusinessDayById(Long businessDayId) {
		return businessDayRepository.findAll().stream()
				.filter(day -> day.getId().equals(businessDayId))
				.findFirst()
				.orElse(null);
	}

	@Override
	public TimeSlot getTimeSlotById(Long timeSlotId) {
		List<BusinessDay> days = businessDayRepository.findAll();
		for (BusinessDay day : days) {
			List<TimeSlot> slots = timeSlotMapper
					.findByBusinessDayId(day.getId());
			for (TimeSlot slot : slots) {
				if (slot.getId() == timeSlotId) {
					return slot;
				}
			}
		}
		return null;
	}

	@Override
	@Transactional
	public void createBooking(Long userId, Long businessDayId,
			Long timeSlotId) {
		List<Booking> bookings = bookingMapper.findByDate(
				businessDayRepository.findAll().stream()
						.filter(day -> day.getId().equals(businessDayId))
						.findFirst()
						.map(BusinessDay::getBusinessDate)
						.orElseThrow(() -> new IllegalArgumentException(
								"営業日が存在しません")));
		for (Booking b : bookings) {
			if (b.getTimeSlotId().equals(timeSlotId)
					&& "reserved".equals(b.getStatus())) {
				throw new IllegalStateException("この枠はすでに予約されています。");
			}
		}

		Booking booking = new Booking();
		booking.setUserId(userId);
		booking.setDate(
				businessDayRepository.findAll().stream()
						.filter(day -> day.getId().equals(businessDayId))
						.findFirst()
						.map(BusinessDay::getBusinessDate)
						.orElse(null));
		booking.setTimeSlotId(timeSlotId);
		booking.setStatus("reserved");
		bookingMapper.insert(booking);
	}

	public List<Booking> getBookingsForBusinessDay(Long businessDayId) {
		BusinessDay day = businessDayRepository.findAll().stream()
				.filter(d -> d.getId().equals(businessDayId))
				.findFirst()
				.orElse(null);
		if (day == null)
			return Collections.emptyList();
		return bookingMapper.findByDate(day.getBusinessDate());
	}
}