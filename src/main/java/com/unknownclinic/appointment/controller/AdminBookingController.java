package com.unknownclinic.appointment.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.unknownclinic.appointment.dto.AdminBookingView;
import com.unknownclinic.appointment.dto.AdminBusinessDayView;
import com.unknownclinic.appointment.service.AdminBookingService;
import com.unknownclinic.appointment.service.BusinessDayService;

@Controller
public class AdminBookingController {

	@Autowired
	private AdminBookingService adminBookingService;

	@Autowired
	private BusinessDayService businessDayService;

	@GetMapping("/admin/main")
	public String showAdminBookingList(
			@RequestParam(name = "businessDate", required = false) LocalDate selectedBusinessDate,
			Model model) {

		List<AdminBusinessDayView> businessDays = businessDayService
				.getAllAdminBusinessDayViews();
		model.addAttribute("businessDays", businessDays);
		model.addAttribute("selectedBusinessDate", selectedBusinessDate);

		String selectedBusinessDayLabel = "";
		List<AdminBookingView> timeTable = new ArrayList<>();

		if (selectedBusinessDate != null) {
			// 営業日が存在し、アクティブかチェック
			if (businessDayService.isBusinessDay(selectedBusinessDate)) {
				selectedBusinessDayLabel = selectedBusinessDate.toString();
				timeTable = adminBookingService
						.getTimeSlotBookingsByDate(selectedBusinessDate);
			}
		}

		model.addAttribute("selectedBusinessDayLabel",
				selectedBusinessDayLabel);
		model.addAttribute("timeTable", timeTable);

		return "admin/main";
	}
}