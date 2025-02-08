package com.dq.empportal.controller;

import com.dq.empportal.model.*;
import com.dq.empportal.repository.EmployeeClientInfoRepository;
import com.dq.empportal.repository.EmployeeRepository;
import com.dq.empportal.repository.LeaveRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/leaves")
public class LeaveController {
    @Autowired
    LeaveRepository leaveRepository;
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    private JavaMailSender javaMailSender;
    private final List<Holiday> holidays = new ArrayList<>();


    @PostMapping("/submitLeave")
    public ResponseEntity<String> submitLeaveRequest(@RequestBody LeaveRequest request) {
        // Create a leave object
        Leave leave = new Leave();
        leave.setEmployeeId(request.getEmployeeId());
        leave.setEmployeeName(employeeRepository.findById(request.getEmployeeId()).get().getFirstName());
        leave.setStartDate(request.getStartDate());
        leave.setEndDate(request.getEndDate());
        leave.setType(request.getType());
        leave.setReason(request.getReason());
        leave.setStatus("Pending"); // Status is pending for now

        // Save leave request
        leaveRepository.save(leave);

        // Send email to HR
        String hrEmail = "ayushsv3@gmail.com"; // Replace with HR's email address
        String subject = "New Leave Request Pending Approval";
        String approvalPageUrl = "http://localhost:8080/leave.html"; // Replace with your actual approval page URL
        String emailBody = String.format(
                "Dear HR,%n%nA new leave request has been submitted by %s. Please review and approve the request.%n%n" +
                        "Start Date: %s%nEnd Date: %s%nType: %s%nReason: %s%n%n" +
                        "You can review and take action using the following link:%n%s%n%n" +
                        "Best regards,%nYour Leave Management System",
                leave.getEmployeeName(),
                leave.getStartDate(),
                leave.getEndDate(),
                leave.getType(),
                leave.getReason(),
                approvalPageUrl
        );

        try {
            sendEmail(hrEmail, subject, emailBody);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Leave request submitted, but failed to send email notification.");
        }

        return ResponseEntity.ok("Leave request submitted successfully and is pending approval.");
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            // Create a MimeMessage
            MimeMessage message = javaMailSender.createMimeMessage();

            // Use MimeMessageHelper to set advanced email properties
            MimeMessageHelper helper = new MimeMessageHelper(message, false); // 'false' indicates this is a simple email

            // Set recipient, subject, and body
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // 'true' indicates the text supports HTML (optional)

            // Set the sender email with a custom display name
            helper.setFrom("digiquadayush@gmail.com", "No-Reply");

            // Send the email
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/approveLeave/{leaveId}")
    public ResponseEntity<String> approveLeave(@PathVariable int leaveId) {
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        if (!"Pending".equals(leave.getStatus())) {
            return ResponseEntity.badRequest().body("Leave request is not pending approval.");
        }

        leave.setStatus("Approved");
        leave.setApprovalDate(LocalDate.now());

        Employee employee=employeeRepository.findById(leave.getEmployeeId()).get();
        employee.getLeaveDays().add(leave);  // Add approved leave and recalculate leave days

        employeeRepository.save(employee);  // Save updated employee-client info
        leaveRepository.save(leave);  // Save the leave with updated status

        return ResponseEntity.ok("Leave request approved successfully.");
    }


    @PostMapping("/rejectLeave/{leaveId}")
    public ResponseEntity<String> rejectLeave(@PathVariable Integer leaveId, @RequestBody Map<String, String> requestBody) {
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        if (!leave.getStatus().equals("Pending")) {
            return ResponseEntity.badRequest().body("Leave request is not pending approval.");
        }

        String rejectionReason = requestBody.get("reason");
        if (rejectionReason == null || rejectionReason.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Rejection reason is required.");
        }

        leave.setStatus("Rejected");  // Update status to rejected
        leave.setRejectionReason(rejectionReason);  // Set rejection reason (Assuming this field exists in Leave entity)
        leaveRepository.save(leave);  // Save the leave request with rejected status and reason

        return ResponseEntity.ok("Leave request rejected. Reason: " + rejectionReason);
    }


    @GetMapping()
    public ResponseEntity<List<Leave>> getAllLeaves(){
        List<Leave> leaves=leaveRepository.findAll();
        return ResponseEntity.ok(leaves);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<List<Leave>> getLeavesByEmployeeId(@PathVariable Integer employeeId) {
        List<Leave> leaves =leaveRepository.findByEmployeeId(employeeId);
        return ResponseEntity.ok(leaves);
    }

    @GetMapping("/{employeeId}/date-range")
    public List<Leave> getLeavesWithinDateRange(
            @PathVariable Integer employeeId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return leaveRepository.findLeavesWithinDateRange(employeeId, startDate, endDate);
    }


}
