package com.dq.empportal.controller;

import com.dq.empportal.model.Holiday;
import com.dq.empportal.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/holidays")
@CrossOrigin("*") // Allows frontend calls from different origins
public class HolidayController {

    @Autowired
    private HolidayService holidayService;

    // ✅ Add multiple holidays (with reason)
    @PostMapping
    public List<Holiday> addHolidays(@RequestBody List<Holiday> holidays) {
        return holidayService.addHolidays(holidays);
    }

    // ✅ Get all holidays
    @GetMapping
    public List<Holiday> getAllHolidays() {
        return holidayService.getAllHolidays();
    }

    @DeleteMapping("/{date}")
    public ResponseEntity<String> removeHoliday(@PathVariable LocalDate date) {
        boolean removed = holidayService.removeHolidayByDate(date);
        if (removed) {
            return ResponseEntity.ok("Holiday on " + date + " removed successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Holiday not found for date: " + date);
        }
    }
}