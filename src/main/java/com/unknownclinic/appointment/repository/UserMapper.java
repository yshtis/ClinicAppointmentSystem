package com.unknownclinic.appointment.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.unknownclinic.appointment.domain.User;

@Mapper
public interface UserMapper {

	User findByCardNumber(@Param("cardNumber") String cardNumber);

	User findByCardNumberAndBirthday(@Param("cardNumber") String cardNumber,
			@Param("birthday") String birthday);

	int updatePasswordByCardNumberAndBirthday(
			@Param("cardNumber") String cardNumber,
			@Param("birthday") String birthday,
			@Param("encodedPassword") String encodedPassword);

	User findById(@Param("id") Long id);

	void insert(User user);

	List<User> findAll();
}