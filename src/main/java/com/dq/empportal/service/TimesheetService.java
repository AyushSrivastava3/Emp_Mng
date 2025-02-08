package com.dq.empportal.service;

import com.dq.empportal.model.Employee;
import com.dq.empportal.model.Task;
import com.dq.empportal.model.Timesheet;
import com.dq.empportal.repository.EmployeeRepository;
import com.dq.empportal.repository.TaskRepository;
import com.dq.empportal.repository.TimesheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TimesheetService {

    @Autowired
    private TimesheetRepository timesheetRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private EmailNotificationService emailNotificationService;

//    public Timesheet createOrUpdateTimesheet(Integer employeeId, Date weekStartDate, List<Task> tasks) {
//        Employee employee = employeeRepository.findById(employeeId)
//                .orElseThrow(() -> new RuntimeException("Employee not found"));
//
//        Timesheet timesheet = timesheetRepository.findByEmployeeIdAndWeekStartDate(employeeId, weekStartDate)
//                .orElse(new Timesheet());
//
//        timesheet.setEmployeeId(employeeId);
//        timesheet.setWeekStartDate(weekStartDate);
//        timesheet.setTasks(tasks);
//
//        tasks.forEach(task -> task.setTimesheet(timesheet));
//        return timesheetRepository.save(timesheet);
//    }
public Timesheet createOrUpdateTimesheet(Integer employeeId, Date weekStartDate, List<Task> tasks) {
    // Fetch the employee from the database
    Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found"));

    // Fetch the existing timesheet or create a new one
    Timesheet timesheet = timesheetRepository.findByEmployeeIdAndWeekStartDate(employeeId, weekStartDate)
            .orElse(new Timesheet());

    // Set the employee and week start date
    timesheet.setEmployeeId(employeeId);
    timesheet.setWeekStartDate(weekStartDate);
    timesheet.setSubmitted(true);
    timesheet.setSubmittedAt(LocalDateTime.now());
    timesheet.setApprovalStatus("Pending");

    // Clear the existing tasks to avoid orphan issues
    timesheet.getTasks().clear();

    // Set the new tasks to the timesheet
    timesheet.getTasks().addAll(tasks);

    // Ensure the bidirectional relationship is maintained
    tasks.forEach(task -> task.setTimesheet(timesheet));

    // Save the updated or new timesheet
    // Save the updated or new timesheet
    Timesheet savedTimesheet = timesheetRepository.save(timesheet);

    // Trigger the email notification asynchronously
    String managerEmail = "ayushsv3@gmail.com"; // Replace with the actual manager email logic
    emailNotificationService.sendTimesheetSubmissionEmail(managerEmail, employeeId, weekStartDate);

    return savedTimesheet;
}


    public Timesheet getTimesheet(Integer employeeId, Date weekStartDate) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        return timesheetRepository.findByEmployeeIdAndWeekStartDate(employeeId, weekStartDate)
                .orElseThrow(() -> new RuntimeException("Timesheet not found"));
    }

    public void saveTimesheet(Timesheet timesheet) {
        timesheetRepository.save(timesheet);
    }

    public Timesheet updateApprovalStatus(Integer timesheetId, String approvalStatus) {
        Optional<Timesheet> optionalTimesheet = timesheetRepository.findById(timesheetId);

        if (optionalTimesheet.isEmpty()) {
            throw new IllegalArgumentException("Timesheet with ID " + timesheetId + " not found");
        }

        Timesheet timesheet = optionalTimesheet.get();
        timesheet.setApprovalStatus(approvalStatus);
        return timesheetRepository.save(timesheet);
    }

    public List<Timesheet> getPendingTimesheets() {
        return timesheetRepository.findByApprovalStatus("Pending");
    }
}

