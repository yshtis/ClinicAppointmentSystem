package com.unknownclinic.appointment.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.unknownclinic.appointment.dto.AdminBookingView;
import com.unknownclinic.appointment.service.AdminBookingService;

@Controller
public class AdminBookingController {

	@GetMapping("/admin/main.html")
	public String showAdminBookingList() {
		return "admin/main";
	}

	@ResponseBody
	@GetMapping("/admin/appointments")
	public List<AdminBookingView> getTimeSlotBookingsByDate(
			@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		return adminBookingService.getTimeSlotBookingsByDate(date);
	}

	@Autowired
	private AdminBookingService adminBookingService;
}