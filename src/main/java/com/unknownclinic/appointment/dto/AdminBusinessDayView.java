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

	// コンストラクタ
	public AdminBusinessDayView() {
	}

	public AdminBusinessDayView(BusinessDay businessDay) {
		this.id = businessDay.getId();
		this.businessDate = businessDay.getBusinessDate().toString();
		this.isActive = businessDay.getIsActive();
	}

	/**
	 * 画面表示用の営業日ラベル生成
	 * フォーマット: "yyyy/MM/dd (曜日) 【営業状態】"
	 */
	public String getBusinessDayLabel() {
		String statusLabel = isActive ? "【営業中】" : "【休業】";
		try {
			LocalDate date = LocalDate.parse(businessDate);
			DateTimeFormatter fmt = DateTimeFormatter
					.ofPattern("yyyy/MM/dd (E)", Locale.JAPANESE);
			return date.format(fmt) + " " + statusLabel;
		} catch (Exception e) {
			return businessDate + " " + statusLabel;
		}
	}

	/**
	 * 営業状態に応じたCSSクラス
	 */
	public String getStatusClass() {
		return isActive ? "text-success" : "text-muted";
	}

	/**
	 * 営業状態切り替えボタンのテキスト
	 */
	public String getToggleButtonText() {
		return isActive ? "休業にする" : "営業再開";
	}

	/**
	 * 営業状態切り替えボタンのCSSクラス
	 */
	public String getToggleButtonClass() {
		return isActive ? "btn-warning" : "btn-success";
	}

	// === 日付操作・判定メソッド ===

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
		return String.format("AdminBusinessDayView[id=%d, date=%s, active=%s]",
				id, businessDate, isActive);
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