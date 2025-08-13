package com.unknownclinic.appointment.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.unknownclinic.appointment.domain.BusinessDay;
import com.unknownclinic.appointment.domain.User;
import com.unknownclinic.appointment.dto.AdminBusinessDayView;
import com.unknownclinic.appointment.dto.TimeSlotView;
import com.unknownclinic.appointment.repository.UserMapper;
import com.unknownclinic.appointment.service.BookingService;
import com.unknownclinic.appointment.service.BusinessDayService;

@Controller
public class BookingController {

	@Autowired
	private BookingService bookingService;

	@Autowired
	private BusinessDayService businessDayService;

	@Autowired
	private UserMapper userMapper;

	@GetMapping("/main")
	public String showBookingForm(
			Model model,
			@AuthenticationPrincipal UserDetails userDetails,
			@RequestParam(name = "businessDate", required = false) LocalDate selectedBusinessDate) {

		String cardNumber = userDetails.getUsername();
		User user = userMapper.findByCardNumber(cardNumber);

		if (user == null) {
			model.addAttribute("error", "ユーザー情報が取得できません。再ログインしてください。");
			return "error";
		}

		// 営業日一覧を営業形態付きで取得
		List<AdminBusinessDayView> businessDayViews = businessDayService
				.getAllAdminBusinessDayViews();
		// アクティブな営業日のみフィルタリング
		List<AdminBusinessDayView> activeBusinessDays = businessDayViews
				.stream()
				.filter(AdminBusinessDayView::isValidBusinessDay)
				.toList();

		model.addAttribute("businessDays", activeBusinessDays);
		model.addAttribute("selectedBusinessDate", selectedBusinessDate);

		// 選択された営業日の営業形態を考慮した時間枠を取得
		List<TimeSlotView> slotViews = businessDayService
				.getAvailableTimeSlotsByBusinessType(selectedBusinessDate);
		model.addAttribute("allTimeSlots", slotViews);

		// 予約済みの時間枠IDを取得
		Set<Long> bookedSlotIds = bookingService
				.getBookedSlotIdsForBusinessDate(selectedBusinessDate);
		model.addAttribute("bookedSlotIds", bookedSlotIds);

		// 選択された営業日の営業形態情報をモデルに追加
		if (selectedBusinessDate != null) {
			BusinessDay selectedBusinessDay = businessDayService
					.getBusinessDayByDate(selectedBusinessDate);
			if (selectedBusinessDay != null) {
				model.addAttribute("selectedBusinessType",
						selectedBusinessDay.getBusinessType());
				model.addAttribute("selectedBusinessTypeName",
						selectedBusinessDay.getBusinessTypeDisplayName());
			}
		}

		return "main";
	}

	@GetMapping("/confirm")
	public String showConfirm(
			@RequestParam Long timeSlotId,
			@RequestParam LocalDate businessDate,
			@RequestParam(required = false) String completed,
			@RequestParam(required = false) String error,
			@AuthenticationPrincipal UserDetails userDetails,
			Model model) {

		String cardNumber = userDetails.getUsername();
		User user = userMapper.findByCardNumber(cardNumber);

		if (user == null) {
			model.addAttribute("error", "ユーザー情報が取得できません。再ログインしてください。");
			return "error";
		}

		// 営業形態チェック（予約確認時点でも営業時間内かを確認）
		BusinessDay businessDay = businessDayService
				.getBusinessDayByDate(businessDate);
		if (businessDay == null || !businessDay.getIsActive()) {
			model.addAttribute("error", "選択した日付は営業日ではありません。");
			return "error";
		}

		TimeSlotView slotView = bookingService
				.getTimeSlotViewBySlotId(timeSlotId, businessDate);

		// 選択した時間枠が営業形態に適しているかチェック
		if (slotView != null && !slotView
				.isAvailableForBusinessType(businessDay.getBusinessType())) {
			model.addAttribute("error", "選択した時間は営業時間外です。");
			return "error";
		}

		model.addAttribute("slotView", slotView);
		model.addAttribute("cardNumber", cardNumber);
		model.addAttribute("timeSlotId", timeSlotId);
		model.addAttribute("businessDate", businessDate);
		model.addAttribute("businessTypeName",
				businessDay.getBusinessTypeDisplayName());

		if (completed != null)
			model.addAttribute("completed", true);
		if (error != null)
			model.addAttribute("errorMessage", error);

		return "confirm";
	}

	@PostMapping("/confirm")
	public String confirmBooking(
			@RequestParam Long timeSlotId,
			@RequestParam LocalDate businessDate,
			@AuthenticationPrincipal UserDetails userDetails,
			Model model,
			RedirectAttributes redirectAttributes) {

		String cardNumber = userDetails.getUsername();
		User user = userMapper.findByCardNumber(cardNumber);

		if (user == null) {
			model.addAttribute("error", "ユーザー情報が取得できません。再ログインしてください。");
			return "error";
		}

		try {
			// 予約作成前に営業形態の最終チェック
			BusinessDay businessDay = businessDayService
					.getBusinessDayByDate(businessDate);
			if (businessDay == null || !businessDay.getIsActive()) {
				throw new IllegalStateException("選択した日付は営業日ではありません。");
			}

			// 時間枠の営業形態適合性チェック
			TimeSlotView slotView = bookingService
					.getTimeSlotViewBySlotId(timeSlotId, businessDate);
			if (slotView != null && !slotView.isAvailableForBusinessType(
					businessDay.getBusinessType())) {
				throw new IllegalStateException("選択した時間は営業時間外です。");
			}

			bookingService.createBooking(user.getId(), businessDate,
					timeSlotId);

			redirectAttributes.addAttribute("timeSlotId", timeSlotId);
			redirectAttributes.addAttribute("businessDate", businessDate);
			redirectAttributes.addAttribute("completed", "true");
			return "redirect:/confirm";

		} catch (IllegalStateException e) {
			redirectAttributes.addAttribute("timeSlotId", timeSlotId);
			redirectAttributes.addAttribute("businessDate", businessDate);
			redirectAttributes.addAttribute("error", e.getMessage());
			return "redirect:/confirm";
		}
	}
}