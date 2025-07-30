package com.unknownclinic.appointment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unknownclinic.appointment.domain.User;
import com.unknownclinic.appointment.repository.UserMapper;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;

	public User findByCardNumber(String cardNumber) {
		return userMapper.findByCardNumber(cardNumber);
	}
}