package com.unknownclinic.appointment.controller;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.unknownclinic.appointment.domain.BusinessDay;
import com.unknownclinic.appointment.service.BusinessDayService;

@Controller
public class MainController {
	private final BusinessDayService businessDayService;

	public MainController(BusinessDayService businessDayService) {
		this.businessDayService = businessDayService;
	}

	@GetMapping("/main")
	public String showMain(Model model) {
		int year = 2025;
		int month = 9;
		YearMonth ym = YearMonth.of(year, month);

		// 営業日のリストを取得（yyyy-MM-dd形式でMap化）
		List<BusinessDay> businessDays = businessDayService.findByMonth(year,
				month);
		Map<String, Boolean> businessDayMap = businessDays.stream()
				.collect(Collectors.toMap(
						bd -> bd.getDate()
								.format(DateTimeFormatter.ISO_LOCAL_DATE),
						BusinessDay::isOpen));

		// カレンダー表示に必要な情報
		int firstDayOfWeek = ym.atDay(1).getDayOfWeek().getValue() % 7; // 日曜=0, 月曜=1...
		int lastDay = ym.lengthOfMonth();

		// Modelへセット
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		model.addAttribute("businessDayMap", businessDayMap);
		model.addAttribute("firstDayOfWeek", firstDayOfWeek);
		model.addAttribute("lastDay", lastDay);

		return "main";
	}
}