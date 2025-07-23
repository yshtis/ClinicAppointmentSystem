package com.unknownclinic.appointment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

	@GetMapping("/admin/admin.html")
	public String adminHome() {
		return "admin/admin";
	}
}