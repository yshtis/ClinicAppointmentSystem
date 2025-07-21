package com.unknownclinic.appointment.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate(); // セッションを破棄
		// ログアウトメッセージをクエリパラメータで渡す
		return "redirect:/login?logout";
	}
}