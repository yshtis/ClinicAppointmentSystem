package com.unknownclinic.appointment.service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unknownclinic.appointment.domain.BusinessDay;
import com.unknownclinic.appointment.dto.AdminBusinessDayView;
import com.unknownclinic.appointment.repository.BusinessDayMapper;

@Service
public class BusinessDayServiceImpl implements BusinessDayService {

	@Autowired
	private BusinessDayMapper businessDayMapper;

	@Override
	public List<BusinessDay> getAllBusinessDays() {
		return businessDayMapper.findAll();
	}

	@Override
	public List<BusinessDay> getActiveBusinessDays() {
		return businessDayMapper.findAllActive();
	}

	@Override
	public BusinessDay getBusinessDayById(Long id) {
		return businessDayMapper.findById(id);
	}

	@Override
	public BusinessDay getBusinessDayByDate(LocalDate date) {
		return businessDayMapper.findByDate(date);
	}

	@Override
	public List<AdminBusinessDayView> getAllAdminBusinessDayViews() {
		return getAllBusinessDays().stream()
				.map(AdminBusinessDayView::new)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public boolean addBusinessDay(String dateString) {
		try {
			LocalDate date = LocalDate.parse(dateString);
			return addBusinessDay(date);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("不正な日付形式です: " + dateString);
		}
	}

	@Override
	@Transactional
	public boolean addBusinessDay(LocalDate date) {
		// 過去の日付チェック
		if (date.isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("過去の日付は営業日として追加できません。");
		}

		// 重複チェック
		if (businessDayMapper.countByBusinessDate(date) > 0) {
			return false;
		}

		BusinessDay businessDay = new BusinessDay();
		businessDay.setBusinessDate(date);
		businessDay.setIsActive(true);
		businessDayMapper.insert(businessDay);
		return true;
	}

	@Override
	@Transactional
	public boolean deleteBusinessDayById(Long id) {
		BusinessDay businessDay = businessDayMapper.findById(id);
		if (businessDay == null) {
			return false;
		}

		// 予約がある営業日は削除不可（将来的にチェック追加予定）
		// if (hasBookings(businessDay.getBusinessDate())) {
		//     throw new IllegalStateException("予約がある営業日は削除できません。");
		// }

		businessDayMapper.deleteById(id);
		return true;
	}

	@Override
	@Transactional
	public boolean deleteBusinessDayByDate(LocalDate date) {
		if (businessDayMapper.countByBusinessDate(date) == 0) {
			return false;
		}
		businessDayMapper.deleteByBusinessDate(date);
		return true;
	}

	@Override
	@Transactional
	public boolean toggleBusinessDayStatus(Long id) {
		BusinessDay businessDay = businessDayMapper.findById(id);
		if (businessDay == null) {
			return false;
		}

		businessDay.setIsActive(!businessDay.getIsActive());
		businessDayMapper.update(businessDay);
		return true;
	}

	@Override
	public boolean isBusinessDay(LocalDate date) {
		BusinessDay businessDay = businessDayMapper.findByDate(date);
		return businessDay != null && businessDay.getIsActive();
	}
}