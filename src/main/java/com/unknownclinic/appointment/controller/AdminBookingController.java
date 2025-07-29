package com.unknownclinic.appointment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminBookingController {

	@GetMapping("/admin/main.html")
	public String adminHome() {
		return "admin/main";
	}
}