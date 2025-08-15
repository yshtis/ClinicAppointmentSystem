package com.unknownclinic.appointment.service;

import java.time.LocalDate;
import java.util.List;

import com.unknownclinic.appointment.domain.BusinessDay;
import com.unknownclinic.appointment.dto.AdminBusinessDayView;
import com.unknownclinic.appointment.dto.TimeSlotView;

public interface BusinessDayService {

	List<BusinessDay> getAllBusinessDays();

	List<BusinessDay> getActiveBusinessDays();

	BusinessDay getBusinessDayById(Long id);

	BusinessDay getBusinessDayByDate(LocalDate date);

	List<AdminBusinessDayView> getAllAdminBusinessDayViews();

	boolean addBusinessDay(String dateString);

	boolean addBusinessDay(LocalDate date);

	boolean addBusinessDayWithType(String dateString, String businessType);

	boolean deleteBusinessDayById(Long id);

	boolean deleteBusinessDayByDate(LocalDate date);

	boolean toggleBusinessDayStatus(Long id);

	boolean isBusinessDay(LocalDate date);

	List<TimeSlotView> getAvailableTimeSlotsByBusinessType(
			LocalDate businessDate);
}