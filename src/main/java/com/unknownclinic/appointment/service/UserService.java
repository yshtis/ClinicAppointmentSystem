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
	
	public User findByCardNumberAndBirthday(String cardNumber, String birthday) {
        return userMapper.findByCardNumberAndBirthday(cardNumber, birthday);
    }

    public boolean resetPassword(String cardNumber, String birthday, String encodedPassword) {
        int updated = userMapper.updatePasswordByCardNumberAndBirthday(cardNumber, birthday, encodedPassword);
        return updated > 0;
    }
}