package com.unknownclinic.appointment.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.unknownclinic.appointment.service.AdminUserDetailsService;
import com.unknownclinic.appointment.service.PatientUserDetailsService;

@Configuration
public class SecurityConfig {

	@Autowired
	private AdminUserDetailsService adminUserDetailsService;

	@Autowired
	private PatientUserDetailsService patientUserDetailsService;

	@Bean
	public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http)
			throws Exception {
		http
				.securityMatcher("/admin/**")
				.authorizeHttpRequests(authz -> authz
						.anyRequest().hasRole("ADMIN"))
				.formLogin(form -> form
						.loginPage("/admin/login")
						.loginProcessingUrl("/admin/login")
						.usernameParameter("loginId")
						.passwordParameter("password")
						.defaultSuccessUrl("/admin/main.html", true)
						.failureUrl("/admin/login?error")
						.permitAll())
				.logout(logout -> logout
						.logoutUrl("/admin/logout")
						.logoutSuccessUrl("/admin/login?logout"))
				.userDetailsService(adminUserDetailsService)
				.csrf(csrf -> csrf.ignoringRequestMatchers("/admin/login"));

		return http.build();
	}

	@Bean
	public SecurityFilterChain userSecurityFilterChain(HttpSecurity http)
			throws Exception {
		http
				.securityMatcher("/login", "/main", "/mypage", "/confirm",
						"/logout")
				.authorizeHttpRequests(authz -> authz
						.requestMatchers("/login", "/logout").permitAll()
						.anyRequest().hasRole("USER"))
				.formLogin(form -> form
						.loginPage("/login")
						.loginProcessingUrl("/login")
						.usernameParameter("cardNumber")
						.passwordParameter("password")
						.defaultSuccessUrl("/main", true)
						.failureUrl("/login?error"))
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/login?logout"))
				.userDetailsService(patientUserDetailsService)
				.csrf(csrf -> csrf.ignoringRequestMatchers("/login"));

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}