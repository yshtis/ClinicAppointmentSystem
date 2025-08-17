package com.unknownclinic.appointment.service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unknownclinic.appointment.domain.BusinessDay;
import com.unknownclinic.appointment.domain.TimeSlot;
import com.unknownclinic.appointment.dto.AdminBusinessDayView;
import com.unknownclinic.appointment.dto.TimeSlotView;
import com.unknownclinic.appointment.repository.BusinessDayMapper;
import com.unknownclinic.appointment.repository.TimeSlotMapper;

@Service
public class BusinessDayServiceImpl implements BusinessDayService {

	@Autowired
	private BusinessDayMapper businessDayMapper;

	@Autowired
	private TimeSlotMapper timeSlotMapper;

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
		if (date.isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("過去の日付は営業日として追加できません。");
		}

		if (businessDayMapper.countByBusinessDate(date) > 0) {
			return false;
		}

		BusinessDay businessDay = new BusinessDay();
		businessDay.setBusinessDate(date);
		businessDay.setIsActive(true);
		businessDay.setBusinessType("allday");
		businessDayMapper.insert(businessDay);

		return true;
	}

	@Override
	@Transactional
	public boolean addBusinessDayWithType(String dateString, String businessType) {
		try {
			LocalDate date = LocalDate.parse(dateString);
			return addBusinessDayWithType(date, businessType);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("不正な日付形式です: " + dateString);
		}
	}

	@Transactional
	public boolean addBusinessDayWithType(LocalDate date, String businessType) {
		if (date.isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("過去の日付は営業日として追加できません。");
		}

		if (businessDayMapper.countByBusinessDate(date) > 0) {
			return false;
		}

		if (!isValidBusinessType(businessType)) {
			businessType = "allday";
		}

		BusinessDay businessDay = new BusinessDay();
		businessDay.setBusinessDate(date);
		businessDay.setIsActive(true);
		businessDay.setBusinessType(businessType);
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
	
	@Override
	public boolean isBusinessDayForAdmin(LocalDate date) {
		BusinessDay businessDay = businessDayMapper.findByDate(date);
		return businessDay != null;
	}

	@Override
	public List<TimeSlotView> getAvailableTimeSlotsByBusinessType(LocalDate businessDate) {
		BusinessDay businessDay = businessDayMapper.findByDate(businessDate);
		if (businessDay == null || !businessDay.getIsActive()) {
			return List.of();
		}

		List<TimeSlot> allTimeSlots = timeSlotMapper.findAll();
		String businessType = businessDay.getBusinessType();

		if (businessType == null) {
			businessType = "allday";
		}

		switch (businessType) {
		case "am":
			return allTimeSlots.stream()
					.filter(slot -> slot.getStartTime().getHour() < 12)
					.map(slot -> new TimeSlotView(slot.getId(),
							slot.getStartTime(),
							slot.getEndTime(), businessDate))
					.collect(Collectors.toList());

		case "pm":
			return allTimeSlots.stream()
					.filter(slot -> slot.getStartTime().getHour() >= 12)
					.map(slot -> new TimeSlotView(slot.getId(),
							slot.getStartTime(),
							slot.getEndTime(), businessDate))
					.collect(Collectors.toList());

		case "allday":
		default:
			return allTimeSlots.stream()
					.map(slot -> new TimeSlotView(slot.getId(),
							slot.getStartTime(),
							slot.getEndTime(), businessDate))
					.collect(Collectors.toList());
		}
	}

	private boolean isValidBusinessType(String businessType) {
		return "allday".equals(businessType) || "am".equals(businessType)
				|| "pm".equals(businessType);
	}
}