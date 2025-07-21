package com.unknownclinic.appointment.controller;

import java.time.LocalDate;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.unknownclinic.appointment.domain.User;
import com.unknownclinic.appointment.service.UserService;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public String login(
			@RequestParam("cardNumber") String cardNumber,
			@RequestParam("birth_year") int year,
			@RequestParam("birth_month") int month,
			@RequestParam("birth_day") int day,
			HttpSession session) {
		LocalDate birthday = LocalDate.of(year, month, day);
		User user = userService.findByCardNumberAndBirthday(cardNumber,
				birthday);

		if (user != null) {
			session.setAttribute("loginUser", user);
			return "redirect:/main";
		} else {
			return "redirect:/login?error";
		}
	}

	@GetMapping("/login")
	public String showLoginForm() {
		return "login";
	}
}
