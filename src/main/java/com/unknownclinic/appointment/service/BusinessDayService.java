package com.unknownclinic.appointment.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unknownclinic.appointment.domain.BusinessDay;
import com.unknownclinic.appointment.domain.BusinessDaySlot;
import com.unknownclinic.appointment.domain.TimeSlotMaster;
import com.unknownclinic.appointment.dto.AdminBusinessDayView;
import com.unknownclinic.appointment.repository.BusinessDayMapper;
import com.unknownclinic.appointment.repository.BusinessDaySlotMapper;
import com.unknownclinic.appointment.repository.TimeSlotMasterMapper;

@Service
public class BusinessDayService {

	@Autowired
	private BusinessDayMapper businessDayMapper;

	@Autowired
	private TimeSlotMasterMapper timeSlotMasterMapper;

	@Autowired
	private BusinessDaySlotMapper businessDaySlotMapper;

	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter
			.ofPattern("yyyy-MM-dd");

	public List<String> getAllBusinessDayStrings() {
		List<BusinessDay> days = businessDayMapper.findAll();
		return days.stream()
				.map(b -> b.getBusinessDate().format(DATE_FORMAT))
				.sorted()
				.collect(Collectors.toList());
	}

	public boolean addBusinessDayWithSlots(String dateStr, String type) {
		LocalDate date = parseDate(dateStr);
		if (date == null)
			return false;
		// 既存判定
		boolean exists = businessDayMapper.findAll().stream()
				.anyMatch(b -> b.getBusinessDate().equals(date));
		if (exists)
			return false;
		// 1. 営業日insert
		BusinessDay day = new BusinessDay();
		day.setBusinessDate(date);
		day.setCreatedAt(LocalDateTime.now());
		businessDayMapper.insert(day);

		// 2. time_slot_master取得し、type別にslotを決定
		List<TimeSlotMaster> timeSlots = timeSlotMasterMapper.findAll(); // am/pm区分あり
		List<Long> slotIds = new ArrayList<>();
		if ("allday".equals(type)) {
			slotIds = timeSlots.stream().map(TimeSlotMaster::getId)
					.collect(Collectors.toList());
		} else if ("am".equals(type)) {
			slotIds = timeSlots.stream().filter(ts -> "am".equals(ts.getAmPm()))
					.map(TimeSlotMaster::getId).collect(Collectors.toList());
		} else if ("pm".equals(type)) {
			slotIds = timeSlots.stream().filter(ts -> "pm".equals(ts.getAmPm()))
					.map(TimeSlotMaster::getId).collect(Collectors.toList());
		}
		for (Long slotId : slotIds) {
			BusinessDaySlot slot = new BusinessDaySlot();
			slot.setBusinessDayId(day.getId());
			slot.setTimeSlotMasterId(slotId);
			slot.setAvailable(true);
			businessDaySlotMapper.insert(slot);
		}
		return true;
	}

	public boolean deleteBusinessDay(String dateStr) {
		LocalDate date = parseDate(dateStr);
		if (date == null)
			return false;
		boolean exists = businessDayMapper.findAll().stream()
				.anyMatch(b -> b.getBusinessDate().equals(date));
		if (!exists)
			return false;
		businessDayMapper.deleteByBusinessDate(date);
		return true;
	}

	public boolean isValidDate(String dateStr) {
		return parseDate(dateStr) != null;
	}

	private LocalDate parseDate(String dateStr) {
		try {
			return LocalDate.parse(dateStr, DATE_FORMAT);
		} catch (DateTimeParseException e) {
			return null;
		}
	}

	public List<AdminBusinessDayView> getAllAdminBusinessDayViews() {
		List<BusinessDay> days = businessDayMapper.findAll();
		List<TimeSlotMaster> timeSlots = timeSlotMasterMapper.findAll();

		List<AdminBusinessDayView> result = new ArrayList<>();
		for (BusinessDay day : days) {
			List<BusinessDaySlot> slots = businessDaySlotMapper
					.findByBusinessDayId(day.getId());
			boolean hasAm = false, hasPm = false;
			for (BusinessDaySlot slot : slots) {
				TimeSlotMaster tsm = timeSlots.stream()
						.filter(ts -> ts.getId()
								.equals(slot.getTimeSlotMasterId()))
						.findFirst().orElse(null);
				if (tsm == null)
					continue;
				if ("am".equals(tsm.getAmPm()))
					hasAm = true;
				if ("pm".equals(tsm.getAmPm()))
					hasPm = true;
			}
			String type = (hasAm && hasPm) ? "allday"
					: (hasAm ? "am" : (hasPm ? "pm" : "unknown"));
			AdminBusinessDayView view = new AdminBusinessDayView();
			view.setId(day.getId());
			view.setBusinessDate(day.getBusinessDate().format(DATE_FORMAT));
			view.setType(type);
			result.add(view);
		}
		result.sort(
				Comparator.comparing(AdminBusinessDayView::getBusinessDate));
		return result;
	}
	
	public boolean deleteBusinessDayById(Long id) {
	    BusinessDay day = businessDayMapper.findById(id);
	    if (day == null) return false;
	    businessDayMapper.deleteById(id);
	    return true;
	}
}