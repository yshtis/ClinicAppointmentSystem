package com.unknownclinic.appointment.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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

		List<BusinessDay> businessDays = bookingService.getBusinessDays();

		// 時間枠ID
		List<String> allTimeSlotIds = Arrays.asList(
				"1", "2", "3", "4", "5", "6", "7",
				"8", "9", "10", "11", "12", "13", "14");
		model.addAttribute("allTimeSlotIds", allTimeSlotIds);

		// 予約済み枠ID
		Set<String> bookedSlotIds = new HashSet<>();
		if (selectedBusinessDayId != null) {
			List<Booking> bookings = bookingService
					.getBookingsForBusinessDay(selectedBusinessDayId);
			bookedSlotIds = bookings.stream()
					.filter(b -> "reserved".equals(b.getStatus()))
					.map(b -> String.valueOf(b.getTimeSlotId()))
					.collect(Collectors.toSet());
		}
		model.addAttribute("bookedSlotIds", bookedSlotIds);
		model.addAttribute("businessDays", businessDays);
		model.addAttribute("selectedBusinessDayId", selectedBusinessDayId);

		return "main";
	}

	@PostMapping("/confirm")
	public String confirmBooking(
			@RequestParam Long businessDayId,
			@RequestParam String timeSlotId,
			@AuthenticationPrincipal UserDetails userDetails,
			Model model,
			@RequestParam(required = false) String confirmed) {

		String cardNumber = userDetails.getUsername();
		User user = userMapper.findByCardNumber(cardNumber);

		if (user == null) {
			model.addAttribute("error", "ユーザー情報が取得できません。再ログインしてください。");
			return "error";
		}

		BusinessDay day = bookingService.getBusinessDayById(businessDayId);
		TimeSlot slot = bookingService
				.getTimeSlotById(Long.parseLong(timeSlotId));

		model.addAttribute("day", day);
		model.addAttribute("slot", slot);

		if ("true".equals(confirmed)) {
			bookingService.createBooking(user.getId(), businessDayId,
					Long.parseLong(timeSlotId));
			model.addAttribute("completed", true);
		}

		return "confirm";
	}
}