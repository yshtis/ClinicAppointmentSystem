package com.unknownclinic.appointment.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unknownclinic.appointment.domain.User;
import com.unknownclinic.appointment.repository.UserMapper;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;

	public User findByCardNumberAndBirthday(String cardNumber,
			LocalDate birthday) {
		return userMapper.findByCardNumberAndBirthday(cardNumber, birthday);
	}
}