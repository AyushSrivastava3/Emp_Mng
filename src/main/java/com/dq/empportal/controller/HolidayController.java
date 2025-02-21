package com.dq.empportal.controller;

import com.dq.empportal.model.Holiday;
import com.dq.empportal.model.HolidayRequest;
import com.dq.empportal.service.HolidayService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

    @PostMapping("/employee")
    public ResponseEntity<String> addHolidayForEmployee(
            @RequestParam String email,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate holidayDate) throws MessagingException {
        boolean added = holidayService.requestHoliday(email, holidayDate);
        if (added) {
            return ResponseEntity.ok("Holiday request added successfully for employee with email: " + email);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add holiday request for employee with email: " + email);
        }
    }

    @GetMapping("/employee")
    public ResponseEntity<List<LocalDate>> getHolidaysByEmployeeEmail(@RequestParam String email) {
        List<LocalDate> holidayDates = holidayService.getHolidayDatesByEmployeeEmail(email);
        if (holidayDates.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.ok(holidayDates);
        }
    }

    @PostMapping("/approve/{requestId}")
    public ResponseEntity<String> approveHolidayRequest(
            @PathVariable Long requestId,
            @RequestParam boolean isApproved,
            @RequestParam(required = false) String managerRemarks) throws MessagingException {

        boolean result = holidayService.approveHolidayRequest(requestId, isApproved, managerRemarks);
        if (result) {
            return ResponseEntity.ok("Holiday request has been " + (isApproved ? "APPROVED" : "REJECTED"));
        } else {
            return ResponseEntity.badRequest().body("Holiday request not found!");
        }
    }

    // ✅ API for Manager to View All Pending Holiday Requests
    @GetMapping("/requests")
    public ResponseEntity<List<HolidayRequest>> getAllHolidayRequests() {
        List<HolidayRequest> requests = holidayService.getAllPendingRequests();
        return ResponseEntity.ok(requests);
    }
}