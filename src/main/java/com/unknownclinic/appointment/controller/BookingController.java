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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.unknownclinic.appointment.domain.Booking;
import com.unknownclinic.appointment.domain.BusinessDay;
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

	// メイン画面: 営業日・時間枠選択
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

		// 時間枠IDリスト
		List<String> allTimeSlotIds = Arrays.asList(
				"1", "2", "3", "4", "5", "6", "7",
				"8", "9", "10", "11", "12", "13", "14");
		model.addAttribute("allTimeSlotIds", allTimeSlotIds);

		// 予約済み時間枠ID
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

	// 予約内容確認画面（GET: PRGパターンで予約完了メッセージ表示にも使う）
	@GetMapping("/confirm")
	public String showConfirm(@RequestParam Long businessDayId,
			@RequestParam String timeSlotId,
			@RequestParam(required = false) String completed,
			@RequestParam(required = false) String error,
			@AuthenticationPrincipal UserDetails userDetails,
			Model model) {
		String cardNumber = userDetails.getUsername();
		User user = userMapper.findByCardNumber(cardNumber);

		if (user == null) {
			model.addAttribute("error", "ユーザー情報が取得できません。再ログインしてください。");
			return "error";
		}

		BusinessDay businessDay = bookingService
				.getBusinessDayById(businessDayId);
		Long timeSlotIdLong = Long.parseLong(timeSlotId);
		String slotLabel = BookingServiceImpl.SLOT_TIME_LABELS
				.get(timeSlotIdLong);

		model.addAttribute("businessDay", businessDay);
		model.addAttribute("timeSlotId", timeSlotIdLong);
		model.addAttribute("slotLabel", slotLabel);
		model.addAttribute("cardNumber", cardNumber);
		if (completed != null)
			model.addAttribute("completed", true);
		if (error != null)
			model.addAttribute("errorMessage", error);

		return "confirm";
	}

	// 予約確定処理（POST: 予約登録 & PRGリダイレクト）
	@PostMapping("/confirm")
	public String confirmBooking(
			@RequestParam Long businessDayId,
			@RequestParam String timeSlotId,
			@AuthenticationPrincipal UserDetails userDetails,
			Model model,
			RedirectAttributes redirectAttributes) {

		String cardNumber = userDetails.getUsername();
		User user = userMapper.findByCardNumber(cardNumber);

		if (user == null) {
			model.addAttribute("error", "ユーザー情報が取得できません。再ログインしてください。");
			return "error";
		}

		try {
			bookingService.createBooking(user.getId(), businessDayId,
					Long.parseLong(timeSlotId));
			// 予約完了後はGET /confirm?completed=true にリダイレクト
			redirectAttributes.addAttribute("businessDayId", businessDayId);
			redirectAttributes.addAttribute("timeSlotId", timeSlotId);
			redirectAttributes.addAttribute("completed", "true");
			return "redirect:/confirm";
		} catch (IllegalStateException e) {
			// 二重予約などのエラー時もリダイレクト
			redirectAttributes.addAttribute("businessDayId", businessDayId);
			redirectAttributes.addAttribute("timeSlotId", timeSlotId);
			redirectAttributes.addAttribute("error", e.getMessage());
			return "redirect:/confirm";
		}
	}
}