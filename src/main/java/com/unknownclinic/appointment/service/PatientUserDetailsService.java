package com.unknownclinic.appointment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.unknownclinic.appointment.domain.User;
import com.unknownclinic.appointment.repository.UserMapper;

@Service
public class PatientUserDetailsService implements UserDetailsService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public UserDetails loadUserByUsername(String cardNumber)
			throws UsernameNotFoundException {
		User user = userMapper.findByCardNumber(cardNumber);
		if (user == null) {
			throw new UsernameNotFoundException(
					"User not found: " + cardNumber);
		}

		// パスワードはDB格納のハッシュ値
		String password = user.getPassword();

		return org.springframework.security.core.userdetails.User
				.withUsername(user.getCardNumber())
				.password(password)
				.roles("USER")
				.build();
	}
}