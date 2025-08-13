package com.unknownclinic.appointment.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.unknownclinic.appointment.domain.BusinessDay;
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
		// 日付昇順ソートを追加
		businessDays = businessDays.stream()
				.sorted(Comparator
						.comparing(AdminBusinessDayView::getBusinessDate))
				.toList();
		model.addAttribute("businessDays", businessDays);
		model.addAttribute("selectedBusinessDate", selectedBusinessDate);

		String selectedBusinessDayLabel = "";
		List<AdminBookingView> timeTable = new ArrayList<>();
		String selectedBusinessTypeName = "";

		if (selectedBusinessDate != null) {
			if (businessDayService.isBusinessDay(selectedBusinessDate)) {
				BusinessDay selectedBusinessDay = businessDayService
						.getBusinessDayByDate(selectedBusinessDate);

				DateTimeFormatter fmt = DateTimeFormatter.ofPattern("M月d日(E)",
						Locale.JAPANESE);
				selectedBusinessDayLabel = selectedBusinessDate.format(fmt);

				selectedBusinessTypeName = selectedBusinessDay
						.getBusinessTypeDisplayName();

				timeTable = adminBookingService
						.getTimeSlotBookingsByDateAndBusinessType(
								selectedBusinessDate,
								selectedBusinessDay.getBusinessType());
				// nullスロット除去
				timeTable = timeTable.stream()
						.filter(Objects::nonNull)
						.toList();
			}
		}

		model.addAttribute("selectedBusinessDayLabel",
				selectedBusinessDayLabel);
		model.addAttribute("selectedBusinessTypeName",
				selectedBusinessTypeName);
		model.addAttribute("timeTable", timeTable);

		return "admin/main";
	}
}