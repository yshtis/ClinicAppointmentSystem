package com.unknownclinic.appointment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.unknownclinic.appointment.dto.AdminBusinessDayView;
import com.unknownclinic.appointment.service.BusinessDayService;

@Controller
@RequestMapping("/admin/business-days")
public class AdminBusinessDayController {

	@Autowired
	private BusinessDayService businessDayService;

	@GetMapping
	public String businessDaysPage(Model model) {
		List<AdminBusinessDayView> businessDays = businessDayService
				.getAllAdminBusinessDayViews();
		model.addAttribute("businessDays", businessDays);
		return "admin/business-days";
	}

	@PostMapping("/add")
	public String addBusinessDay(
			@RequestParam String date,
			@RequestParam String type,
			RedirectAttributes ra) {
		boolean added = businessDayService.addBusinessDayWithSlots(date, type);
		if (!added) {
			ra.addFlashAttribute("error", "既に営業日です。");
		} else {
			ra.addFlashAttribute("message", "営業日を追加しました。");
		}
		return "redirect:/admin/business-days";
	}

	@PostMapping("/delete")
	public String deleteBusinessDay(
			@RequestParam("id") Long id,
			RedirectAttributes ra) {
		boolean deleted = businessDayService.deleteBusinessDayById(id);
		if (!deleted) {
			ra.addFlashAttribute("error", "営業日が見つかりません。");
		} else {
			ra.addFlashAttribute("message", "営業日を削除しました。");
		}
		return "redirect:/admin/business-days";
	}
}