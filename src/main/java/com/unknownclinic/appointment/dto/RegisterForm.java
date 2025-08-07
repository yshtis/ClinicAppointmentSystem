package com.unknownclinic.appointment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class RegisterForm {

	@NotBlank(message = "お名前は必須です")
	@Size(max = 20, message = "お名前は20文字以内で入力してください")
	private String name;

	@NotBlank(message = "生年月日は必須です")
	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "生年月日はYYYY-MM-DD形式で入力してください")
	private String birthday;

	@NotBlank(message = "電話番号は必須です")
	@Pattern(regexp = "^0\\d{9,10}$", message = "正しい電話番号を入力してください")
	private String phoneNumber;

	@NotBlank(message = "パスワードは必須です")
	@Size(min = 6, max = 32, message = "パスワードは6～32文字で入力してください")
	private String password;

	@NotBlank(message = "確認用パスワードは必須です")
	private String passwordConfirm;
}