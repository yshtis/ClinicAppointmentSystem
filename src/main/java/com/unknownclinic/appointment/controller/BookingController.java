package com.unknownclinic.appointment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookingController {

	@GetMapping("/main")
	public String showBookingForm() {
		return "main";
	}
}