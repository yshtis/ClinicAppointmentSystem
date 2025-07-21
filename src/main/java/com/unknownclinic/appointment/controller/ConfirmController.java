package com.unknownclinic.appointment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConfirmController {

    @GetMapping("/confirm")
    public String showConfirm(Model model) {
        return "confirm";  // templates/confirm.html をレンダリング
    }
}

