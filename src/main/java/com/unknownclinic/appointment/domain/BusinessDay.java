package com.unknownclinic.appointment.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class BusinessDay {

	private Long id;

	@NotNull
	private LocalDate businessDate;

	private Boolean isActive = true;

	private String businessType = "allday";

	private LocalDateTime createdAt;

	// 営業形態の表示名取得メソッド
	public String getBusinessTypeDisplayName() {
		if (businessType == null)
			return "終日営業";

		switch (businessType) {
		case "allday":
			return "終日営業";
		case "am":
			return "午前営業";
		case "pm":
			return "午後営業";
		default:
			return "終日営業";
		}
	}

	// 営業形態のCSSクラス取得メソッド
	public String getBusinessTypeCssClass() {
		if (businessType == null)
			return "btn-primary";

		switch (businessType) {
		case "allday":
			return "btn-primary";
		case "am":
			return "btn-warning";
		case "pm":
			return "btn-info";
		default:
			return "btn-primary";
		}
	}
}