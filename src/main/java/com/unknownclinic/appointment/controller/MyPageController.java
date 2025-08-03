package com.unknownclinic.appointment.controller;

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

import com.unknownclinic.appointment.domain.User;
import com.unknownclinic.appointment.dto.BookingView;
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
		String cardNumber = authentication.getName();
		User user = userService.findByCardNumber(cardNumber);

		// 予約一覧取得（View用DTOで日付・ラベル付きで返す）
		List<BookingView> allBookings = bookingService
				.getBookingViewsByUser(user.getId());

		List<BookingView> futureBookings = allBookings.stream()
				.filter(BookingView::isActiveFutureBooking)
				.sorted(Comparator.comparing(BookingView::getBusinessDate)
						.thenComparing(BookingView::getSlotLabel))
				.collect(Collectors.toList());
		List<BookingView> pastBookings = allBookings.stream()
				.filter(b -> !b.isActiveFutureBooking())
				.sorted(Comparator.comparing(BookingView::getBusinessDate)
						.reversed()
						.thenComparing(BookingView::getSlotLabel).reversed())
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

		bookingService.cancelBooking(bookingId, user.getId());

		return "redirect:/mypage";
	}
}