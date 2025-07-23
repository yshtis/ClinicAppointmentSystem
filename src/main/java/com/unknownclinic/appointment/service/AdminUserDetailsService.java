package com.unknownclinic.appointment.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.unknownclinic.appointment.domain.Admin;
import com.unknownclinic.appointment.repository.AdminMapper;

@Service
public class AdminUserDetailsService implements UserDetailsService {

	@Autowired
	private AdminMapper adminMapper;

	@Override
	public UserDetails loadUserByUsername(String loginId)
			throws UsernameNotFoundException {
		Admin admin = adminMapper.findByLoginId(loginId);
		if (admin == null)
			throw new UsernameNotFoundException("管理者が見つかりません: " + loginId);

		return new User(
				admin.getLoginId(),
				admin.getPassword(),
				Collections.singletonList(
						new SimpleGrantedAuthority("ROLE_ADMIN")));
	}
}