package com.unknownclinic.appointment.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.unknownclinic.appointment.service.AdminUserDetailsService;
import com.unknownclinic.appointment.service.PatientUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private AdminUserDetailsService adminUserDetailsService;

	@Autowired
	private PatientUserDetailsService patientUserDetailsService;

	@Bean
	@Order(1)
	public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http)
			throws Exception {
		http
				.securityMatcher("/admin/**")
				.authorizeHttpRequests(authz -> authz
						.requestMatchers(
								"/admin/login",
								"/admin/logout",
								"/css/**",
								"/js/**",
								"/images/**",
								"/admin/css/**",
								"/admin/js/**",
								"/admin/images/**")
						.permitAll()
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.anyRequest().denyAll())
				.formLogin(form -> form
						.loginPage("/admin/login")
						.loginProcessingUrl("/admin/login")
						.usernameParameter("loginId")
						.passwordParameter("password")
						.defaultSuccessUrl("/admin/main", true)
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
	@Order(2)
	public SecurityFilterChain userSecurityFilterChain(HttpSecurity http)
			throws Exception {
		http
				.securityMatcher("/**")
				.authorizeHttpRequests(authz -> authz
						.requestMatchers("/admin/**").denyAll()
						.requestMatchers(
								"/login",
								"/logout",
								"/reset-password",
								"/register",
								"/css/**",
								"/js/**",
								"/images/**",
								"/error"
						).permitAll()
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
				.csrf(csrf -> csrf.ignoringRequestMatchers("/login",
						"/register"));

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}