package com.unknownclinic.appointment.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.unknownclinic.appointment.domain.BusinessDay;
import com.unknownclinic.appointment.domain.User;
import com.unknownclinic.appointment.dto.TimeSlotView;
import com.unknownclinic.appointment.repository.UserMapper;
import com.unknownclinic.appointment.service.BookingService;

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
		model.addAttribute("businessDays", businessDays);
		model.addAttribute("selectedBusinessDayId", selectedBusinessDayId);

		// 1. 営業日枠リスト
		List<TimeSlotView> slotViews = bookingService
				.getTimeSlotsForView(selectedBusinessDayId);
		model.addAttribute("allTimeSlots", slotViews);

		// 2. 予約済み枠（businessDaySlotIdセット）
		Set<Long> bookedSlotIds = bookingService
				.getBookedSlotIdsForBusinessDay(selectedBusinessDayId);
		model.addAttribute("bookedSlotIds", bookedSlotIds);

		return "main";
	}

	// 予約内容確認画面
	@GetMapping("/confirm")
	public String showConfirm(
			@RequestParam Long businessDaySlotId,
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

		// 枠情報の取得（営業日・時間枠ラベルなどを含むDTO）
		TimeSlotView slotView = bookingService
				.getTimeSlotViewByBusinessDaySlotId(businessDaySlotId);

		model.addAttribute("slotView", slotView);
		model.addAttribute("cardNumber", cardNumber);
		model.addAttribute("businessDaySlotId", businessDaySlotId); // hidden用

		if (completed != null)
			model.addAttribute("completed", true);
		if (error != null)
			model.addAttribute("errorMessage", error);

		return "confirm";
	}

	// 予約確定処理
	@PostMapping("/confirm")
	public String confirmBooking(
			@RequestParam Long businessDaySlotId,
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
			bookingService.createBooking(user.getId(), businessDaySlotId);
			redirectAttributes.addAttribute("businessDaySlotId",
					businessDaySlotId);
			redirectAttributes.addAttribute("completed", "true");
			return "redirect:/confirm";
		} catch (IllegalStateException e) {
			redirectAttributes.addAttribute("businessDaySlotId",
					businessDaySlotId);
			redirectAttributes.addAttribute("error", e.getMessage());
			return "redirect:/confirm";
		}
	}
}