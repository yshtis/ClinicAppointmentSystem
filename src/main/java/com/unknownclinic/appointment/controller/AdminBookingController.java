package com.unknownclinic.appointment.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
			@RequestParam(name = "businessDayId", required = false) Long selectedBusinessDayId,
			Model model) {
		List<AdminBusinessDayView> businessDays = businessDayService
				.getAllAdminBusinessDayViews();
		model.addAttribute("businessDays", businessDays);
		model.addAttribute("selectedBusinessDayId", selectedBusinessDayId);

		String selectedBusinessDayLabel = "";
		List<AdminBookingView> timeTable = new ArrayList<>();
		if (selectedBusinessDayId != null) {
			Optional<AdminBusinessDayView> selected = businessDays.stream()
					.filter(d -> d.getId().equals(selectedBusinessDayId))
					.findFirst();
			if (selected.isPresent()) {
				selectedBusinessDayLabel = selected.get().getBusinessDate(); // yyyy-MM-dd
				if (selectedBusinessDayLabel != null
						&& !selectedBusinessDayLabel.isEmpty()) {
					timeTable = adminBookingService.getTimeSlotBookingsByDate(
							java.time.LocalDate
									.parse(selectedBusinessDayLabel));
				}
			}
		}
		model.addAttribute("selectedBusinessDayLabel",
				selectedBusinessDayLabel);
		model.addAttribute("timeTable", timeTable);

		return "admin/main";
	}
}