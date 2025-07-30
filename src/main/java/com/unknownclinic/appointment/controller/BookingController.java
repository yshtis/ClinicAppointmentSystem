package com.unknownclinic.appointment.controller;

import java.util.List;
import java.util.Map;

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
			@AuthenticationPrincipal UserDetails userDetails) {

		// 診察券番号（ユーザー名）でユーザー情報を取得
		String cardNumber = userDetails.getUsername();
		User user = userMapper.findByCardNumber(cardNumber);

		if (user == null) {
			model.addAttribute("error", "ユーザー情報が取得できません。再ログインしてください。");
			return "error";
		}

		Long userId = user.getId();

		List<BusinessDay> businessDays = bookingService.getBusinessDays();
		Map<Long, List<TimeSlot>> timeSlotsMap = bookingService
				.getAllTimeSlotsGroupedByBusinessDay();
		Map<Long, Booking> userBookings = bookingService
				.getUserBookingsForBusinessDays(userId, businessDays);

		// 時間枠ID→ラベルも渡す
		Map<Long, String> slotTimeLabels = ((BookingServiceImpl) bookingService)
				.getSlotTimeLabels();

		model.addAttribute("businessDays", businessDays);
		model.addAttribute("timeSlotsMap", timeSlotsMap);
		model.addAttribute("userBookings", userBookings);
		model.addAttribute("slotTimeLabels", slotTimeLabels);
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