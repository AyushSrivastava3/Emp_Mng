package com.dq.empportal.service;

import com.dq.empportal.model.Holiday;
import com.dq.empportal.repository.HolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HolidayService {

    @Autowired
    private HolidayRepository holidayRepository;

    // ✅ Add multiple holidays (with date + reason)
    public List<Holiday> addHolidays(List<Holiday> holidays) {
        return holidayRepository.saveAll(holidays);
    }

    // ✅ Get all holidays
    public List<Holiday> getAllHolidays() {
        return holidayRepository.findAll();
    }

    public boolean removeHolidayByDate(LocalDate date) {
        Optional<Holiday> holiday = holidayRepository.findByDate(date);
        if (holiday.isPresent()) {
            holidayRepository.delete(holiday.get());
            return true;
        }
        return false;
    }
}