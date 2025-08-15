package com.unknownclinic.appointment.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
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
	public String businessDaysPage(Model model, CsrfToken token) {
		model.addAttribute("_csrf", token);
		List<AdminBusinessDayView> businessDays = businessDayService
				.getAllAdminBusinessDayViews();
		businessDays = businessDays.stream()
				.sorted(Comparator
						.comparing(AdminBusinessDayView::getBusinessDate))
				.toList();
		model.addAttribute("businessDays", businessDays);
		return "admin/business-days";
	}

	@PostMapping("/add")
	public String addBusinessDay(
			@RequestParam String date,
			@RequestParam(defaultValue = "allday") String businessType,
			RedirectAttributes ra) {
		try {
			boolean added = businessDayService.addBusinessDayWithType(date,
					businessType);
			if (!added) {
				ra.addFlashAttribute("error", "すでに営業日として設定されています。");
			} else {
				ra.addFlashAttribute("message", "営業日を追加しました。");
			}
		} catch (Exception e) {
			ra.addFlashAttribute("error", "営業日の追加に失敗しました: " + e.getMessage());
		}
		return "redirect:/admin/business-days";
	}

	@PostMapping("/delete")
	public String deleteBusinessDay(
			@RequestParam("id") Long id,
			RedirectAttributes ra) {
		try {
			boolean deleted = businessDayService.deleteBusinessDayById(id);
			if (!deleted) {
				ra.addFlashAttribute("error", "営業日が見つかりません。");
			} else {
				ra.addFlashAttribute("message", "営業日を削除しました。");
			}
		} catch (Exception e) {
			ra.addFlashAttribute("error", "営業日の削除に失敗しました: " + e.getMessage());
		}
		return "redirect:/admin/business-days";
	}

	@PostMapping("/toggle")
	public String toggleBusinessDayStatus(
			@RequestParam("id") Long id,
			RedirectAttributes ra) {
		try {
			boolean toggled = businessDayService.toggleBusinessDayStatus(id);
			if (!toggled) {
				ra.addFlashAttribute("error", "営業日が見つかりません。");
			} else {
				ra.addFlashAttribute("message", "営業日の状態を変更しました。");
			}
		} catch (Exception e) {
			ra.addFlashAttribute("error", "営業日の状態変更に失敗しました: " + e.getMessage());
		}
		return "redirect:/admin/business-days";
	}
}