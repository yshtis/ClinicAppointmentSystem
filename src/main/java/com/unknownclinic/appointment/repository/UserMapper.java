package com.unknownclinic.appointment.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.unknownclinic.appointment.domain.User;

@Mapper
public interface UserMapper {
	// 診察券番号でユーザー取得（Spring Security認証用）
	User findByCardNumber(@Param("cardNumber") String cardNumber);

	// IDでユーザー取得（必要に応じて）
	User findById(@Param("id") Long id);

	// 新規登録
	void insert(User user);

	// 全ユーザー取得（管理用など）
	List<User> findAll();
}