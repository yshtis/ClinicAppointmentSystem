package com.unknownclinic.appointment.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unknownclinic.appointment.domain.Booking;
import com.unknownclinic.appointment.domain.BusinessDay;
import com.unknownclinic.appointment.domain.BusinessDaySlot;
import com.unknownclinic.appointment.domain.TimeSlotMaster;
import com.unknownclinic.appointment.dto.BookingView;
import com.unknownclinic.appointment.dto.TimeSlotView;
import com.unknownclinic.appointment.repository.BookingMapper;
import com.unknownclinic.appointment.repository.BusinessDayMapper;
import com.unknownclinic.appointment.repository.BusinessDaySlotMapper;
import com.unknownclinic.appointment.repository.TimeSlotMasterMapper;

@Service
public class BookingServiceImpl implements BookingService {

	@Autowired
	private BusinessDayMapper businessDayMapper;

	@Autowired
	private TimeSlotMasterMapper timeSlotMasterMapper;

	@Autowired
	private BusinessDaySlotMapper businessDaySlotMapper;

	@Autowired
	private BookingMapper bookingMapper;

	@Override
	public List<BusinessDay> getBusinessDays() {
		return businessDayMapper.findAll();
	}

	@Override
	public BusinessDay getBusinessDayById(Long businessDayId) {
		return businessDayMapper.findById(businessDayId);
	}

	public List<TimeSlotView> getTimeSlotsForView(Long businessDayId) {
		if (businessDayId == null)
			return List.of();
		List<BusinessDaySlot> slots = businessDaySlotMapper
				.findAvailableByBusinessDayId(businessDayId);
		List<TimeSlotMaster> masters = timeSlotMasterMapper.findAll();
		return slots.stream()
				.map(slot -> {
					TimeSlotMaster master = masters.stream()
							.filter(m -> m.getId()
									.equals(slot.getTimeSlotMasterId()))
							.findFirst().orElse(null);
					return new TimeSlotView(slot.getId(), master.getLabel());
				})
				.collect(Collectors.toList());
	}

	public Set<Long> getBookedSlotIdsForBusinessDay(Long businessDayId) {
		if (businessDayId == null)
			return Set.of();
		List<BusinessDaySlot> slots = businessDaySlotMapper
				.findAvailableByBusinessDayId(businessDayId);
		List<Long> slotIds = slots.stream().map(BusinessDaySlot::getId)
				.collect(Collectors.toList());
		List<Booking> bookings = bookingMapper
				.findByBusinessDaySlotIds(slotIds);
		return bookings.stream()
				.filter(b -> "reserved".equalsIgnoreCase(b.getStatus()))
				.map(Booking::getBusinessDaySlotId)
				.collect(Collectors.toSet());
	}

	public TimeSlotView getTimeSlotViewByBusinessDaySlotId(
			Long businessDaySlotId) {
		BusinessDaySlot slot = businessDaySlotMapper
				.findById(businessDaySlotId);
		if (slot == null)
			return null;
		TimeSlotMaster master = timeSlotMasterMapper
				.findById(slot.getTimeSlotMasterId());
		BusinessDay day = businessDayMapper.findById(slot.getBusinessDayId());
		return new TimeSlotView(slot.getId(), master.getLabel(),
				day.getBusinessDate());
	}

	@Override
	@Transactional
	public void createBooking(Long userId, Long businessDaySlotId) {
		Booking existing = bookingMapper.findReservedByUserAndSlot(userId,
				businessDaySlotId);
		if (existing != null) {
			throw new IllegalStateException("この枠はすでに予約済みです。");
		}
		Booking booking = new Booking();
		booking.setUserId(userId);
		booking.setBusinessDaySlotId(businessDaySlotId);
		booking.setStatus("reserved");
		bookingMapper.insert(booking);
	}

	@Override
	public List<BookingView> getBookingViewsByUser(Long userId) {
		List<Booking> bookings = bookingMapper.findByUserId(userId);
		List<BusinessDaySlot> slots = businessDaySlotMapper.findByIds(
				bookings.stream().map(Booking::getBusinessDaySlotId)
						.collect(Collectors.toList()));
		List<TimeSlotMaster> masters = timeSlotMasterMapper.findAll();
		List<BusinessDay> days = businessDayMapper.findAll();
		return bookings.stream().map(b -> {
			BusinessDaySlot slot = slots.stream()
					.filter(s -> s.getId().equals(b.getBusinessDaySlotId()))
					.findFirst().orElse(null);
			TimeSlotMaster master = slot != null
					? masters.stream().filter(
							m -> m.getId().equals(slot.getTimeSlotMasterId()))
							.findFirst().orElse(null)
					: null;
			BusinessDay day = slot != null
					? days.stream().filter(
							d -> d.getId().equals(slot.getBusinessDayId()))
							.findFirst().orElse(null)
					: null;
			return new BookingView(b,
					day != null ? day.getBusinessDate() : null,
					master != null ? master.getLabel() : null);
		}).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void cancelBooking(Long bookingId, Long userId) {
		Booking booking = bookingMapper.findById(bookingId);
		if (booking == null || !booking.getUserId().equals(userId)) {
			throw new IllegalArgumentException("予約が存在しないか、権限がありません");
		}
		booking.setStatus("canceled");
		bookingMapper.update(booking);
	}
}