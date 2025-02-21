package com.dq.empportal.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class EmailNotificationService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
//    public void sendTimesheetSubmissionEmail(String managerEmail, Integer employeeId, Date weekStartDate) {
//        try {
//            // Formatting the date for the email content
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            String formattedWeekStartDate = dateFormat.format(weekStartDate);
//            String approvalPageUrl = "http://localhost:8080/pendingTimesheet.html"; // Replace with your actual approval page URL
//
//            // Create the email message
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setTo(managerEmail);
//            message.setSubject("Timesheet Submitted");
//            message.setText("Dear Manager,\n\n" +
//                    "Employee ID: " + employeeId + " has submitted their timesheet for the week starting on " + formattedWeekStartDate + ".\n\n" +
//                    "Please review and take appropriate action.\n\n" +"Pending timesheets"+approvalPageUrl+
//                    "Best regards,\n" +
//                    "Employee Portal System");
//
//            // Send the email
//            mailSender.send(message);
//
//            // Log success
//            System.out.println("Email successfully sent to: " + managerEmail);
//        } catch (Exception e) {
//            // Log failure
//            System.err.println("Error sending email to: " + managerEmail);
//            e.printStackTrace();
//        }
//    }

    public void sendTimesheetSubmissionEmail(String managerEmail, Integer employeeId, Date weekStartDate) {
        try {
            // Formatting the date for the email content
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedWeekStartDate = dateFormat.format(weekStartDate);
            String approvalPageUrl = "http://localhost:8080/pendingTimesheet.html"; // Replace with actual URL

            // Create the email message
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(managerEmail);
            message.setSubject("Timesheet Submitted");
            message.setText("Dear Manager,\n\n" +
                    "Employee ID: " + employeeId + " has submitted their timesheet for the week starting on " + formattedWeekStartDate + ".\n\n" +
                    "Please review and take appropriate action by visiting the link below:\n\n" +
                    approvalPageUrl + "\n\n" + // Ensure the link is properly formatted
                    "Best regards,\n" +
                    "Employee Portal System");

            // Send the email
            mailSender.send(message);

            // Log success
            System.out.println("Email successfully sent to: " + managerEmail);
        } catch (Exception e) {
            // Log failure
            System.err.println("Error sending email to: " + managerEmail);
            e.printStackTrace();
        }
    }

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom("your-email@gmail.com"); // Set your email
        helper.setText(body, true); // `true` indicates HTML content

        mailSender.send(message);
    }

}
