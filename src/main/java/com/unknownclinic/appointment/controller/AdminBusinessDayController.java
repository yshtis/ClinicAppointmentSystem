package com.unknownclinic.appointment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.unknownclinic.appointment.service.BusinessDayService;

@Controller
@RequestMapping("/admin/business-days")
public class AdminBusinessDayController {

	@Autowired
	private BusinessDayService businessDayService;

	@GetMapping
	public String businessDaysPage() {
		return "admin/business-days";
	}

	@GetMapping("/list")
	@ResponseBody
	public List<String> getBusinessDays() {
		return businessDayService.getAllBusinessDayStrings();
	}

	@PostMapping("/add")
	@ResponseBody
	public ResponseEntity<String> addBusinessDay(
			@RequestParam("date") String date,
			@RequestParam("type") String type) {
		if (!businessDayService.isValidDate(date)) {
			return ResponseEntity.badRequest().body("不正な日付形式です。");
		}
		boolean added = businessDayService.addBusinessDayWithSlots(date, type);
		if (!added) {
			return ResponseEntity.badRequest().body("既に営業日です。");
		}
		return ResponseEntity.ok("営業日と枠を追加しました。");
	}

	@PostMapping("/delete")
	@ResponseBody
	public ResponseEntity<String> deleteBusinessDay(
			@RequestParam("date") String date) {
		if (!businessDayService.isValidDate(date)) {
			return ResponseEntity.badRequest().body("不正な日付形式です。");
		}
		boolean deleted = businessDayService.deleteBusinessDay(date);
		if (!deleted) {
			return ResponseEntity.badRequest()
					.body("営業日が見つかりません（削除未実装の可能性あり）。");
		}
		return ResponseEntity.ok("営業日を削除しました。");
	}
}