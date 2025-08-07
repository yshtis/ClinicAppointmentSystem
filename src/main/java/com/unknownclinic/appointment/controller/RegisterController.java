package com.unknownclinic.appointment.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.unknownclinic.appointment.domain.User;
import com.unknownclinic.appointment.dto.RegisterForm;
import com.unknownclinic.appointment.service.UserService;

@Controller
public class RegisterController {

	@Autowired
	private UserService userService;

	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("registerForm", new RegisterForm());
		return "register";
	}

	@PostMapping("/register")
	public String register(
			@ModelAttribute("registerForm") @Valid RegisterForm registerForm,
			BindingResult bindingResult,
			Model model) {
		if (!bindingResult.hasErrors()) {
			if (!registerForm.getPassword()
					.equals(registerForm.getPasswordConfirm())) {
				bindingResult.rejectValue("passwordConfirm",
						"error.passwordConfirm", "パスワードが一致しません");
			}
		}
		if (bindingResult.hasErrors()) {
			model.addAttribute("registerError", true);
			return "register";
		}
		try {
			User user = userService.register(
					registerForm.getName(),
					registerForm.getBirthday(),
					registerForm.getPhoneNumber(),
					registerForm.getPassword());
			model.addAttribute("registerSuccess", true);
			model.addAttribute("issuedCardNumber", user.getCardNumber());
			return "register";
		} catch (Exception e) {
			model.addAttribute("registerError", true);
			return "register";
		}
	}
}