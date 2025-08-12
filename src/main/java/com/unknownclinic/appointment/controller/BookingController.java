package com.unknownclinic.appointment.controller;

import java.time.LocalDate;
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

	@GetMapping("/main")
	public String showBookingForm(
			Model model,
			@AuthenticationPrincipal UserDetails userDetails,
			@RequestParam(name = "businessDate", required = false) LocalDate selectedBusinessDate) {

		String cardNumber = userDetails.getUsername();
		User user = userMapper.findByCardNumber(cardNumber);

		if (user == null) {
			model.addAttribute("error", "ユーザー情報が取得できません。再ログインしてください。");
			return "error";
		}

		List<BusinessDay> businessDays = bookingService.getBusinessDays();
		model.addAttribute("businessDays", businessDays);
		model.addAttribute("selectedBusinessDate", selectedBusinessDate);

		List<TimeSlotView> slotViews = bookingService
				.getTimeSlotsForView(selectedBusinessDate);
		model.addAttribute("allTimeSlots", slotViews);

		Set<Long> bookedSlotIds = bookingService
				.getBookedSlotIdsForBusinessDate(selectedBusinessDate);
		model.addAttribute("bookedSlotIds", bookedSlotIds);

		return "main";
	}

	@GetMapping("/confirm")
	public String showConfirm(
			@RequestParam Long timeSlotId,
			@RequestParam LocalDate businessDate,
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

		TimeSlotView slotView = bookingService
				.getTimeSlotViewBySlotId(timeSlotId, businessDate);

		model.addAttribute("slotView", slotView);
		model.addAttribute("cardNumber", cardNumber);
		model.addAttribute("timeSlotId", timeSlotId);
		model.addAttribute("businessDate", businessDate);

		if (completed != null)
			model.addAttribute("completed", true);
		if (error != null)
			model.addAttribute("errorMessage", error);

		return "confirm";
	}

	@PostMapping("/confirm")
	public String confirmBooking(
			@RequestParam Long timeSlotId,
			@RequestParam LocalDate businessDate,
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
			bookingService.createBooking(user.getId(), businessDate,
					timeSlotId);
			redirectAttributes.addAttribute("timeSlotId", timeSlotId);
			redirectAttributes.addAttribute("businessDate", businessDate);
			redirectAttributes.addAttribute("completed", "true");
			return "redirect:/confirm";
		} catch (IllegalStateException e) {
			redirectAttributes.addAttribute("timeSlotId", timeSlotId);
			redirectAttributes.addAttribute("businessDate", businessDate);
			redirectAttributes.addAttribute("error", e.getMessage());
			return "redirect:/confirm";
		}
	}
}