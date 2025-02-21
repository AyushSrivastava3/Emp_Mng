package com.dq.empportal.service;

import com.dq.empportal.model.Employee;
import com.dq.empportal.model.Holiday;
import com.dq.empportal.model.HolidayRequest;
import com.dq.empportal.repository.EmployeeRepository;
import com.dq.empportal.repository.HolidayRepository;
import com.dq.empportal.repository.HolidayRequestRepository;
import jakarta.mail.MessagingException;
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

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private HolidayRequestRepository holidayRequestRepository;
    @Autowired
    private EmailNotificationService emailNotificationService;

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

//    public boolean addHolidayForEmployee(String email, LocalDate holidayDate) {
//        // Find the employee by email
//        Employee employee = employeeRepository.findByProfessionalEmail(email).get();
//        if (employee == null) {
//            return false; // Employee not found
//        }
//
//        // Create a new holiday for the employee
//
//        employee.getHolidays().add(holidayDate);
//
//        // Save the holiday
//        employeeRepository.save(employee);
//
//        return true;
//    }

    public boolean requestHoliday(String email, LocalDate holidayDate) throws MessagingException {
        // Find employee
        Optional<Employee> optionalEmployee = employeeRepository.findByProfessionalEmail(email);
        if (optionalEmployee.isEmpty()) {
            return false; // Employee not found
        }
        Employee employee = optionalEmployee.get();

        // Create a holiday request
        HolidayRequest holidayRequest = new HolidayRequest();
        holidayRequest.setEmployee(employee);
        holidayRequest.setHolidayDate(holidayDate);
        holidayRequest.setStatus("PENDING"); // Default status

        holidayRequestRepository.save(holidayRequest);

        // Notify manager (via email or system notification)
        String managerEmail = employee.getManagerEmail(); // Fetch manager email
        String subject = "Holiday Request from " + employee.getFirstName();
//        String body = "Dear Manager,"
//                +   employee.getFirstName() + " has requested a holiday on <b>" + holidayDate + "</b>.</p>"
//                + "Please review and approve/reject the request.";

        String body = "<html><body>"
                + "<p>Dear Manager,</p>"
                + "<p>" + employee.getFirstName() + " has requested a holiday on <b>" + holidayDate + "</b>.</p>"
                + "<p>Please review and approve/reject the request by clicking the link below:</p>"
                + "<p><a href='http://localhost:8080/holiday_request.html'>View Leave Requests</a></p>"
                + "<br><br>Best regards,<br>HR Team"
                + "</body></html>";




        emailNotificationService.sendEmail(managerEmail, subject, body);
        return true;
    }


    public List<LocalDate> getHolidayDatesByEmployeeEmail(String email) {
        Employee employee = employeeRepository.findByProfessionalEmail(email).get();
        return employee.getHolidays();
    }

    public boolean approveHolidayRequest(Long requestId, boolean isApproved, String managerRemarks) throws MessagingException {
        Optional<HolidayRequest> optionalRequest = holidayRequestRepository.findById(requestId);
        if (optionalRequest.isEmpty()) {
            return false;
        }

        HolidayRequest holidayRequest = optionalRequest.get();
        Employee employee = holidayRequest.getEmployee();

        String status = isApproved ? "APPROVED" : "REJECTED";
        holidayRequest.setStatus(status);
        holidayRequest.setManagerRemarks(managerRemarks);

        if (isApproved) {
            employee.getHolidays().add(holidayRequest.getHolidayDate());
            employeeRepository.save(employee);
        }

        holidayRequestRepository.save(holidayRequest);

        // Send email to the employee
        String subject = "Your Holiday Request has been " + status;
//        String body = "Dear " + employee.getFirstName()
//                + "Your holiday request for <b>" + holidayRequest.getHolidayDate() + "</b> has been <b>" + status + "</b>"
//                + "Manager's Remarks: " + (managerRemarks != null ? managerRemarks : "No remarks") ;
        String body = "Dear " + employee.getFirstName() + ",<br><br>"
                + "Your holiday request for <b>" + holidayRequest.getHolidayDate() + "</b> has been <b>" + status + "</b>.<br><br>"
                + "<b>Manager's Remarks:</b> " + (managerRemarks != null ? managerRemarks : "No remarks") + "<br><br>"
                + "Best regards,<br>HR Team";

        emailNotificationService.sendEmail(employee.getProfessionalEmail(), subject, body);

        return true;
    }

    public List<HolidayRequest> getAllPendingRequests() {
        return holidayRequestRepository.findByStatus("PENDING");
    }
}