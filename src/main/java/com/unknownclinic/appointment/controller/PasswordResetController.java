package com.unknownclinic.appointment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.unknownclinic.appointment.service.UserService;

@Controller
public class PasswordResetController {

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/reset-password")
	public String showResetPasswordForm() {
		return "reset-password";
	}

	@PostMapping("/reset-password")
	public String processResetPassword(
			@RequestParam String cardNumber,
			@RequestParam String birthDate,
			@RequestParam String newPassword,
			@RequestParam String confirmPassword,
			Model model) {

		// 入力バリデーション（例: パスワード一致チェック）
		if (!newPassword.equals(confirmPassword)) {
			model.addAttribute("message", "新しいパスワードが一致しません。");
			return "reset-password";
		}

		// サービス層でユーザー確認と更新
		boolean result = userService.resetPassword(cardNumber, birthDate,
				passwordEncoder.encode(newPassword));
		if (result) {
			model.addAttribute("message", "パスワードを再設定しました。ログイン画面からログインしてください。");
		} else {
			model.addAttribute("message", "診察券番号または生年月日が正しくありません。");
		}
		return "reset-password";
	}
}