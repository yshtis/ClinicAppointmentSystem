package com.unknownclinic.appointment.service;

import com.unknownclinic.appointment.domain.User;

public interface UserService {
	
    User findByCardNumber(String cardNumber);

    User findByCardNumberAndBirthday(String cardNumber, String birthday);

    boolean resetPassword(String cardNumber, String birthday, String encodedPassword);

    User register(String name, String birthday, String phoneNumber, String rawPassword);
}