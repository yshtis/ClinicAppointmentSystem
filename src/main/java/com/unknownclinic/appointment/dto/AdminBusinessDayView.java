package com.unknownclinic.appointment.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.unknownclinic.appointment.domain.BusinessDay;

import lombok.Data;

@Data
public class AdminBusinessDayView {
	private Long id;
	private String rawBusinessDate;
	private Boolean isActive;
	private String businessType; // 営業形態（allday/am/pm）

	public AdminBusinessDayView() {
	}

	public AdminBusinessDayView(BusinessDay businessDay) {
		this.id = businessDay.getId();
		this.rawBusinessDate = businessDay.getBusinessDate() != null
				? businessDay.getBusinessDate().toString()
				: null;
		this.isActive = businessDay.getIsActive() != null
				? businessDay.getIsActive()
				: false;
		this.businessType = businessDay.getBusinessType();
	}

	/** LocalDate形式での日付取得（nullならLocalDate.MIN） */
	public LocalDate getBusinessDate() {
		try {
			return rawBusinessDate != null
					? LocalDate.parse(rawBusinessDate)
					: LocalDate.MIN;
		} catch (Exception e) {
			return LocalDate.MIN;
		}
	}

	public String getBusinessDateLabel() {
		if (rawBusinessDate == null)
			return "未設定";
		try {
			return getBusinessDate().format(
					DateTimeFormatter.ofPattern("yyyy/MM/dd (E)",
							Locale.JAPANESE));
		} catch (Exception e) {
			return rawBusinessDate;
		}
	}

	/** ★ テンプレート互換用：main.html /admin/main.html どちらでも使えるように */
	public String getBusinessDayLabel() {
		return getBusinessDateLabel();
	}

	public String getBusinessTypeDisplayName() {
		if (businessType == null)
			return "終日";
		return switch (businessType) {
		case "am" -> "午前";
		case "pm" -> "午後";
		default -> "終日";
		};
	}

	public String getBusinessTypeLabel() {
		if (businessType == null)
			return "終日";
		return switch (businessType) {
		case "am" -> "午前";
		case "pm" -> "午後";
		default -> "終日";
		};
	}

	public String getBusinessTypeButtonClass() {
		if (businessType == null)
			return "bg-success";
		return switch (businessType) {
		case "am" -> "bg-warning";
		case "pm" -> "bg-info";
		default -> "bg-success";
		};
	}

	public String getStatusIcon() {
		return Boolean.TRUE.equals(isActive) ? "bi-check-circle-fill"
				: "bi-x-circle-fill";
	}

	public boolean isValidBusinessDay() {
		return Boolean.TRUE.equals(isActive) && !isPast();
	}

	public boolean isToday() {
		return getBusinessDate().equals(LocalDate.now());
	}

	public boolean isTomorrow() {
		return getBusinessDate().equals(LocalDate.now().plusDays(1));
	}

	public boolean isPast() {
		return getBusinessDate().isBefore(LocalDate.now());
	}

	public boolean isThisWeek() {
		LocalDate date = getBusinessDate();
		LocalDate today = LocalDate.now();
		return !date.isBefore(today) && date.isBefore(today.plusDays(7));
	}

	public String getDateCssClass() {
		if (isToday())
			return "today";
		if (isTomorrow())
			return "tomorrow";
		if (isPast())
			return "past-soon";
		if (isThisWeek())
			return "this-week";
		return "";
	}

	public String getShortDateLabel() {
		if (rawBusinessDate == null)
			return "未設定";
		try {
			return getBusinessDate().format(
					DateTimeFormatter.ofPattern("M/d(E)", Locale.JAPANESE));
		} catch (Exception e) {
			return rawBusinessDate;
		}
	}
}