package com.unknownclinic.appointment.controller;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.unknownclinic.appointment.domain.Booking;
import com.unknownclinic.appointment.domain.User;
import com.unknownclinic.appointment.service.BookingService;
import com.unknownclinic.appointment.service.UserService;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

	@Autowired
	private BookingService bookingService;

	@Autowired
	private UserService userService;

	@GetMapping
	public String showMyPage(Model model, Authentication authentication) {
		// 診察券番号(ログインID)からUser取得
		String cardNumber = authentication.getName();
		User user = userService.findByCardNumber(cardNumber);

		// 予約一覧取得
		List<Booking> allBookings = bookingService
				.getBookingsByUser(user.getId());

		// 今日以降＝未来の予約・過去の予約に分割
		LocalDate today = LocalDate.now();
		List<Booking> futureBookings = allBookings.stream()
				.filter(b -> !b.getDate().isBefore(today)
						&& !"cancelled".equalsIgnoreCase(b.getStatus()))
				.sorted(Comparator.comparing(Booking::getDate)
						.thenComparing(Booking::getTimeSlotId))
				.collect(Collectors.toList());
		List<Booking> pastBookings = allBookings.stream()
				.filter(b -> b.getDate().isBefore(today)
						|| "cancelled".equalsIgnoreCase(b.getStatus()))
				.sorted(Comparator.comparing(Booking::getDate).reversed()
						.thenComparing(Booking::getTimeSlotId).reversed())
				.collect(Collectors.toList());

		model.addAttribute("user", user);
		model.addAttribute("futureBookings", futureBookings);
		model.addAttribute("pastBookings", pastBookings);

		return "mypage";
	}

	@PostMapping("/cancel")
	public String cancelBooking(@RequestParam("bookingId") Long bookingId,
			Authentication authentication) {
		String cardNumber = authentication.getName();
		User user = userService.findByCardNumber(cardNumber);

		bookingService.cancelBooking(bookingId, user.getId()); // BookingServiceでユーザーID一致確認＆キャンセル

		return "redirect:/mypage";
	}
}