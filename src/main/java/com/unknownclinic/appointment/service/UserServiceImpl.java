package com.unknownclinic.appointment.service;

import java.time.LocalDate;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.unknownclinic.appointment.domain.User;
import com.unknownclinic.appointment.repository.UserMapper;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User findByCardNumber(String cardNumber) {
		return userMapper.findByCardNumber(cardNumber);
	}

	@Override
	public User findByCardNumberAndBirthday(String cardNumber,
			String birthday) {
		return userMapper.findByCardNumberAndBirthday(cardNumber, birthday);
	}

	@Override
	public boolean resetPassword(String cardNumber, String birthday,
			String encodedPassword) {
		int updated = userMapper.updatePasswordByCardNumberAndBirthday(
				cardNumber, birthday, encodedPassword);
		return updated > 0;
	}

	@Override
	public User register(String name, String birthday, String phoneNumber,
			String rawPassword) {
		User user = new User();

		String sanitizedName = NameSanitizer.sanitize(name);
		user.setName(sanitizedName);
		user.setBirthday(LocalDate.parse(birthday));
		user.setPhoneNumber(phoneNumber);
		user.setPassword(passwordEncoder.encode(rawPassword));
		user.setCardNumber("DUMMY");
		userMapper.insert(user);

		String serial = String.format("%05d", user.getId());
		String random = String.format("%02d", new Random().nextInt(100));
		String cardNumber = serial + random;

		user.setCardNumber(cardNumber);
		userMapper.updateCardNumber(user.getId(), cardNumber);

		return user;
	}
}