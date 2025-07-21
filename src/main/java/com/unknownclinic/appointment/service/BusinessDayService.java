package com.unknownclinic.appointment.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.unknownclinic.appointment.domain.BusinessDay;
import com.unknownclinic.appointment.repository.BusinessDayMapper;

@Service
public class BusinessDayService {
	private final BusinessDayMapper businessDayMapper;

	public BusinessDayService(BusinessDayMapper businessDayMapper) {
		this.businessDayMapper = businessDayMapper;
	}

	// LocalDate->Boolean のMapを返す
	public Map<LocalDate, Boolean> getBusinessDayMap(int year, int month) {
		YearMonth yearMonth = YearMonth.of(year, month);
		LocalDate start = yearMonth.atDay(1);
		LocalDate end = yearMonth.atEndOfMonth();
		List<BusinessDay> list = businessDayMapper.findByMonth(start, end);

		Map<LocalDate, Boolean> map = new HashMap<>();
		for (BusinessDay bd : list) {
			map.put(bd.getDate(), bd.isOpen());
		}
		return map;
	}

	// コントローラ用に「指定年月の営業日リスト」を返す
	public List<BusinessDay> findByMonth(int year, int month) {
		YearMonth yearMonth = YearMonth.of(year, month);
		LocalDate start = yearMonth.atDay(1);
		LocalDate end = yearMonth.atEndOfMonth();
		return businessDayMapper.findByMonth(start, end);
	}
}