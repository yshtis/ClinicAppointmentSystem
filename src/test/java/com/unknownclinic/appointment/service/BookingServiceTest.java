package com.unknownclinic.appointment.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.unknownclinic.appointment.domain.Booking;
import com.unknownclinic.appointment.domain.BusinessDay;
import com.unknownclinic.appointment.domain.TimeSlot;
import com.unknownclinic.appointment.dto.TimeSlotView;
import com.unknownclinic.appointment.repository.BookingMapper;
import com.unknownclinic.appointment.repository.BusinessDayMapper;
import com.unknownclinic.appointment.repository.TimeSlotMapper;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

	@Mock
	private BusinessDayMapper businessDayMapper;

	@Mock
	private TimeSlotMapper timeSlotMapper;

	@Mock
	private BookingMapper bookingMapper;

	@InjectMocks
	private BookingServiceImpl bookingService;

	private BusinessDay testBusinessDay;
	private TimeSlot testTimeSlot;
	private Booking testBooking;
	private LocalDate testDate;

	@BeforeEach
	void setUp() {
		testDate = LocalDate.now().plusDays(1);

		testBusinessDay = new BusinessDay();
		testBusinessDay.setId(1L);
		testBusinessDay.setBusinessDate(testDate);
		testBusinessDay.setBusinessType("allday");
		testBusinessDay.setIsActive(true);

		testTimeSlot = new TimeSlot();
		testTimeSlot.setId(1L);
		testTimeSlot.setStartTime(LocalTime.of(9, 0));
		testTimeSlot.setEndTime(LocalTime.of(10, 0));
		testTimeSlot.setIsActive(true);

		testBooking = new Booking();
		testBooking.setId(1L);
		testBooking.setUserId(1L);
		testBooking.setBusinessDate(testDate);
		testBooking.setTimeSlotId(1L);
		testBooking.setStatus("reserved");
	}

	@Test
	void testCreateBooking_Success() {
		// Given
		Long userId = 1L;
		Long timeSlotId = 1L;

		when(businessDayMapper.findByDate(testDate))
				.thenReturn(testBusinessDay);
		when(timeSlotMapper.findById(timeSlotId)).thenReturn(testTimeSlot);
		when(bookingMapper.findReservedBySlot(testDate, timeSlotId))
				.thenReturn(null);
		when(bookingMapper.findReservedByUserAndBusinessDate(userId, testDate))
				.thenReturn(null);

		// When
		assertDoesNotThrow(() -> {
			bookingService.createBooking(userId, testDate, timeSlotId);
		});

		// Then
		verify(bookingMapper, times(1)).insert(any(Booking.class));
	}

	@Test
	void testCreateBooking_BusinessDayNotActive() {
		// Given
		Long userId = 1L;
		Long timeSlotId = 1L;
		testBusinessDay.setIsActive(false);

		when(businessDayMapper.findByDate(testDate))
				.thenReturn(testBusinessDay);

		// When & Then
		IllegalStateException exception = assertThrows(
				IllegalStateException.class, () -> {
					bookingService.createBooking(userId, testDate, timeSlotId);
				});

		assertEquals("指定された日付は営業日ではありません。", exception.getMessage());
		verify(bookingMapper, never()).insert(any(Booking.class));
	}

	@Test
	void testCreateBooking_TimeSlotAlreadyBooked() {
		// Given
		Long userId = 1L;
		Long timeSlotId = 1L;

		when(businessDayMapper.findByDate(testDate))
				.thenReturn(testBusinessDay);
		when(timeSlotMapper.findById(timeSlotId)).thenReturn(testTimeSlot);
		when(bookingMapper.findReservedBySlot(testDate, timeSlotId))
				.thenReturn(testBooking);

		// When & Then
		IllegalStateException exception = assertThrows(
				IllegalStateException.class, () -> {
					bookingService.createBooking(userId, testDate, timeSlotId);
				});

		assertEquals("この枠はすでに予約済みです。", exception.getMessage());
		verify(bookingMapper, never()).insert(any(Booking.class));
	}

	@Test
	void testCreateBooking_UserAlreadyHasBookingOnSameDate() {
		// Given
		Long userId = 1L;
		Long timeSlotId = 1L;

		when(businessDayMapper.findByDate(testDate))
				.thenReturn(testBusinessDay);
		when(timeSlotMapper.findById(timeSlotId)).thenReturn(testTimeSlot);
		when(bookingMapper.findReservedBySlot(testDate, timeSlotId))
				.thenReturn(null);
		when(bookingMapper.findReservedByUserAndBusinessDate(userId, testDate))
				.thenReturn(testBooking);

		// When & Then
		IllegalStateException exception = assertThrows(
				IllegalStateException.class, () -> {
					bookingService.createBooking(userId, testDate, timeSlotId);
				});

		assertEquals("同じ日に複数予約はできません。", exception.getMessage());
		verify(bookingMapper, never()).insert(any(Booking.class));
	}

	@Test
	void testCancelBooking_Success() {
		// Given
		Long bookingId = 1L;
		Long userId = 1L;
		testBooking.setBusinessDate(LocalDate.now().plusDays(1));

		when(bookingMapper.findById(bookingId)).thenReturn(testBooking);

		// When
		assertDoesNotThrow(() -> {
			bookingService.cancelBooking(bookingId, userId);
		});

		// Then
		verify(bookingMapper, times(1)).update(any(Booking.class));
	}

	@Test
	void testCancelBooking_BookingNotFound() {
		// Given
		Long bookingId = 1L;
		Long userId = 1L;

		when(bookingMapper.findById(bookingId)).thenReturn(null);

		// When & Then
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class, () -> {
					bookingService.cancelBooking(bookingId, userId);
				});

		assertEquals("予約が存在しないか、権限がありません", exception.getMessage());
		verify(bookingMapper, never()).update(any(Booking.class));
	}

	@Test
	void testCancelBooking_PastDate() {
		// Given
		Long bookingId = 1L;
		Long userId = 1L;
		testBooking.setBusinessDate(LocalDate.now().minusDays(1));

		when(bookingMapper.findById(bookingId)).thenReturn(testBooking);

		// When & Then
		IllegalStateException exception = assertThrows(
				IllegalStateException.class, () -> {
					bookingService.cancelBooking(bookingId, userId);
				});

		assertEquals("過去の予約はキャンセルできません。", exception.getMessage());
		verify(bookingMapper, never()).update(any(Booking.class));
	}

	@Test
	void testGetTimeSlotsForView_AllDay() {
		// Given
		TimeSlot morningSlot = new TimeSlot();
		morningSlot.setId(1L);
		morningSlot.setStartTime(LocalTime.of(9, 0));
		morningSlot.setEndTime(LocalTime.of(10, 0));
		morningSlot.setIsActive(true);

		TimeSlot afternoonSlot = new TimeSlot();
		afternoonSlot.setId(2L);
		afternoonSlot.setStartTime(LocalTime.of(14, 0));
		afternoonSlot.setEndTime(LocalTime.of(15, 0));
		afternoonSlot.setIsActive(true);

		when(businessDayMapper.findByDate(testDate))
				.thenReturn(testBusinessDay);
		when(timeSlotMapper.findAllActive())
				.thenReturn(Arrays.asList(morningSlot, afternoonSlot));

		// When
		List<TimeSlotView> result = bookingService
				.getTimeSlotsForView(testDate);

		// Then
		assertEquals(2, result.size());
		assertEquals(1L, result.get(0).getId());
		assertEquals(2L, result.get(1).getId());
	}

	@Test
	void testGetTimeSlotsForView_AMOnly() {
		// Given
		testBusinessDay.setBusinessType("am");

		TimeSlot morningSlot = new TimeSlot();
		morningSlot.setId(1L);
		morningSlot.setStartTime(LocalTime.of(9, 0));
		morningSlot.setEndTime(LocalTime.of(10, 0));
		morningSlot.setIsActive(true);

		TimeSlot afternoonSlot = new TimeSlot();
		afternoonSlot.setId(2L);
		afternoonSlot.setStartTime(LocalTime.of(14, 0));
		afternoonSlot.setEndTime(LocalTime.of(15, 0));
		afternoonSlot.setIsActive(true);

		when(businessDayMapper.findByDate(testDate))
				.thenReturn(testBusinessDay);
		when(timeSlotMapper.findAllActive())
				.thenReturn(Arrays.asList(morningSlot, afternoonSlot));

		// When
		List<TimeSlotView> result = bookingService
				.getTimeSlotsForView(testDate);

		// Then
		assertEquals(1, result.size());
		assertEquals(1L, result.get(0).getId());
		assertEquals(LocalTime.of(9, 0), result.get(0).getStartTime());
	}

	@Test
	void testGetTimeSlotsForView_BusinessDayNull() {
		// Given
		when(businessDayMapper.findByDate(testDate)).thenReturn(null);

		// When
		List<TimeSlotView> result = bookingService
				.getTimeSlotsForView(testDate);

		// Then
		assertTrue(result.isEmpty());
		verify(timeSlotMapper, never()).findAllActive();
	}

	@Test
	void testGetBookedSlotIdsForBusinessDate() {
		// Given
		Booking booking1 = new Booking();
		booking1.setTimeSlotId(1L);
		booking1.setStatus("reserved");

		Booking booking2 = new Booking();
		booking2.setTimeSlotId(2L);
		booking2.setStatus("cancelled");

		Booking booking3 = new Booking();
		booking3.setTimeSlotId(3L);
		booking3.setStatus("reserved");

		when(bookingMapper.findByDate(testDate))
				.thenReturn(Arrays.asList(booking1, booking2, booking3));

		// When
		Set<Long> result = bookingService
				.getBookedSlotIdsForBusinessDate(testDate);

		// Then
		assertEquals(2, result.size());
		assertTrue(result.contains(1L));
		assertTrue(result.contains(3L));
		assertFalse(result.contains(2L));
	}

	@Test
	void testGetBookedSlotIdsForBusinessDate_NullDate() {
		// When
		Set<Long> result = bookingService.getBookedSlotIdsForBusinessDate(null);

		// Then
		assertTrue(result.isEmpty());
		verify(bookingMapper, never()).findByDate(any());
	}
}