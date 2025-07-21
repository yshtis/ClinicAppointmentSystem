package com.unknownclinic.appointment.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // /confirm → templates/confirm.html
        registry.addViewController("/confirm").setViewName("confirm");

        // 必要に応じてどんどん追加
        // registry.addViewController("/login").setViewName("login");
    }
}