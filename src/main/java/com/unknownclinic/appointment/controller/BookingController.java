package com.unknownclinic.appointment.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.unknownclinic.appointment.domain.Booking;
import com.unknownclinic.appointment.domain.BusinessDay;
import com.unknownclinic.appointment.domain.TimeSlot;
import com.unknownclinic.appointment.domain.User;
import com.unknownclinic.appointment.repository.UserMapper;
import com.unknownclinic.appointment.service.BookingService;
import com.unknownclinic.appointment.service.BookingServiceImpl;

@Controller
public class BookingController {

	@Autowired
	private BookingService bookingService;

	@Autowired
	private UserMapper userMapper;

	@GetMapping("/main")
	public String showBookingForm(
			Model model,
			@AuthenticationPrincipal UserDetails userDetails,
			@RequestParam(name = "businessDayId", required = false) Long selectedBusinessDayId) {

		String cardNumber = userDetails.getUsername();
		User user = userMapper.findByCardNumber(cardNumber);

		if (user == null) {
			model.addAttribute("error", "ユーザー情報が取得できません。再ログインしてください。");
			return "error";
		}

		Long userId = user.getId();

		List<BusinessDay> businessDays = bookingService.getBusinessDays();

		// 全枠ID（1〜14）
		List<Long> allTimeSlotIds = Arrays.asList(
				1L, 2L, 3L, 4L, 5L, 6L, 7L,
				8L, 9L, 10L, 11L, 12L, 13L, 14L);
		model.addAttribute("allTimeSlotIds", allTimeSlotIds);

		// 選択営業日の予約済み枠ID一覧を取得
		Set<Long> bookedSlotIds = new HashSet<>();
		if (selectedBusinessDayId != null) {
			List<Booking> bookings = bookingService
					.getBookingsForBusinessDay(selectedBusinessDayId);
			bookedSlotIds = bookings.stream()
					.filter(b -> "reserved".equals(b.getStatus()))
					.map(Booking::getTimeSlotId)
					.collect(Collectors.toSet());
		}
		model.addAttribute("bookedSlotIds", bookedSlotIds);

		// 時間枠ID→ラベル
		Map<Long, String> slotTimeLabels = ((BookingServiceImpl) bookingService)
				.getSlotTimeLabels();
		model.addAttribute("slotTimeLabels", slotTimeLabels);

		model.addAttribute("businessDays", businessDays);
		model.addAttribute("selectedBusinessDayId", selectedBusinessDayId);
		return "main";
	}

	@PostMapping("/confirm")
	public String confirmBooking(
			@RequestParam Long businessDayId,
			@RequestParam Long timeSlotId,
			@AuthenticationPrincipal UserDetails userDetails,
			Model model,
			@RequestParam(required = false) String confirmed) {

		String cardNumber = userDetails.getUsername();
		User user = userMapper.findByCardNumber(cardNumber);

		if (user == null) {
			model.addAttribute("error", "ユーザー情報が取得できません。再ログインしてください。");
			return "error";
		}

		Long userId = user.getId();

		BusinessDay day = bookingService.getBusinessDayById(businessDayId);
		TimeSlot slot = bookingService.getTimeSlotById(timeSlotId);

		model.addAttribute("day", day);
		model.addAttribute("slot", slot);

		if ("true".equals(confirmed)) {
			bookingService.createBooking(userId, businessDayId, timeSlotId);
			model.addAttribute("completed", true);
		}

		return "confirm";
	}
}