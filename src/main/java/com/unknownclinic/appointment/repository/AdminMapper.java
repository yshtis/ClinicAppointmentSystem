package com.unknownclinic.appointment.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.unknownclinic.appointment.domain.Admin;

@Mapper
public interface AdminMapper {
	@Select("SELECT * FROM admins WHERE login_id = #{loginId}")
	Admin findByLoginId(String loginId);
}