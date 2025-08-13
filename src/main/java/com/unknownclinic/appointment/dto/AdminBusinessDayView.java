package com.unknownclinic.appointment.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.unknownclinic.appointment.domain.BusinessDay;

import lombok.Data;

@Data
public class AdminBusinessDayView {
	private Long id;
	private String businessDate;
	private Boolean isActive;
	private String businessType; // 営業形態（allday/am/pm）

	// コンストラクタ
	public AdminBusinessDayView() {
	}

	public AdminBusinessDayView(BusinessDay businessDay) {
		this.id = businessDay.getId();
		this.businessDate = businessDay.getBusinessDate().toString();
		this.isActive = businessDay.getIsActive();
		this.businessType = businessDay.getBusinessType();
	}

	/**
	 * 画面表示用の営業日ラベル生成
	 * フォーマット: "yyyy/MM/dd (曜日) 【営業形態】"
	 */
	public String getBusinessDayLabel() {
		try {
			LocalDate date = LocalDate.parse(businessDate);
			DateTimeFormatter fmt = DateTimeFormatter
					.ofPattern("yyyy/MM/dd (E)", Locale.JAPANESE);
			return date.format(fmt);
		} catch (Exception e) {
			return businessDate;
		}
	}

	/**
	 * 営業形態の表示名取得
	 */
	public String getBusinessTypeDisplayName() {
		if (businessType == null)
			return "終日";

		switch (businessType) {
		case "allday":
			return "終日";
		case "am":
			return "午前";
		case "pm":
			return "午後";
		default:
			return "終日";
		}
	}

	/**
	 * 営業形態に応じたCSSクラス
	 */
	public String getStatusClass() {
		if (!isActive)
			return "text-muted";

		if (businessType == null)
			return "text-success";

		switch (businessType) {
		case "allday":
			return "text-success";
		case "am":
			return "text-warning";
		case "pm":
			return "text-info";
		default:
			return "text-success";
		}
	}

	/**
	 * 営業形態ボタンのテキスト
	 */
	public String getBusinessTypeButtonText() {
		return getBusinessTypeDisplayName();
	}

	/**
	 * 営業形態ボタンのCSSクラス（統一デザイン用）
	 * すべて同じ形状でバッジサイズも統一
	 */
	public String getBusinessTypeButtonClass() {
		if (businessType == null)
			return "bg-success";

		switch (businessType) {
		case "allday":
			return "bg-success";
		case "am":
			return "bg-warning";
		case "pm":
			return "bg-info";
		default:
			return "bg-success";
		}
	}

	/**
	 * LocalDate形式での日付取得（ソート用）
	 */
	public LocalDate getBusinessDateAsLocalDate() {
		try {
			return LocalDate.parse(businessDate);
		} catch (Exception e) {
			return LocalDate.MIN; // エラー時は最古の日付として扱う
		}
	}

	/**
	 * 今日かどうかの判定
	 */
	public boolean isToday() {
		return getBusinessDateAsLocalDate().equals(LocalDate.now());
	}

	/**
	 * 明日かどうかの判定
	 */
	public boolean isTomorrow() {
		return getBusinessDateAsLocalDate().equals(LocalDate.now().plusDays(1));
	}

	/**
	 * 過去の日付かどうかの判定
	 */
	public boolean isPast() {
		return getBusinessDateAsLocalDate().isBefore(LocalDate.now());
	}

	/**
	 * 今週内かどうかの判定
	 */
	public boolean isThisWeek() {
		LocalDate date = getBusinessDateAsLocalDate();
		LocalDate today = LocalDate.now();
		return !date.isBefore(today) && date.isBefore(today.plusDays(7));
	}

	/**
	 * CSS用のクラス名生成
	 * 日付の特性に応じたスタイリングクラスを返す
	 */
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

	/**
	 * 営業日の優先度取得（表示順序用）
	 * 数値が小さいほど優先度が高い
	 */
	public int getPriority() {
		if (isToday())
			return 1;
		if (isTomorrow())
			return 2;
		if (isThisWeek())
			return 3;
		if (isPast())
			return 9; // 過去は最低優先度
		return 5; // 通常の未来日付
	}

	/**
	 * 日付の表示形式（短縮版）
	 * モバイル表示等で使用
	 */
	public String getShortDateLabel() {
		try {
			LocalDate date = LocalDate.parse(businessDate);
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("M/d(E)",
					Locale.JAPANESE);
			return date.format(fmt);
		} catch (Exception e) {
			return businessDate;
		}
	}

	/**
	 * 営業日として有効かどうかの総合判定
	 */
	public boolean isValidBusinessDay() {
		return isActive && !isPast();
	}

	/**
	 * 営業状態のアイコン取得
	 */
	public String getStatusIcon() {
		if (isActive) {
			return "bi-check-circle-fill";
		} else {
			return "bi-x-circle-fill";
		}
	}

	/**
	 * 日付の詳細説明テキスト
	 */
	public String getDateDescription() {
		if (isToday())
			return "今日";
		if (isTomorrow())
			return "明日";
		if (isPast())
			return "過去";
		if (isThisWeek())
			return "今週";
		return "";
	}

	// === toString、equals、hashCode（デバッグ用） ===

	@Override
	public String toString() {
		return String.format(
				"AdminBusinessDayView[id=%d, date=%s, active=%s, type=%s]",
				id, businessDate, isActive, businessType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		AdminBusinessDayView that = (AdminBusinessDayView) obj;
		return id != null && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}