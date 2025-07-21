package com.unknownclinic.appointment.repository;

import java.time.LocalDate;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.unknownclinic.appointment.domain.User;

@Mapper
public interface UserMapper {
	User findByCardNumberAndBirthday(@Param("cardNumber") String cardNumber,
			@Param("birthday") LocalDate birthday);
}