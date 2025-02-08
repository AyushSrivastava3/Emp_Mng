package com.dq.empportal.controller;

import com.dq.empportal.dtos.TimesheetRequest;
import com.dq.empportal.model.Task;
import com.dq.empportal.model.Timesheet;
import com.dq.empportal.service.TimesheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/timesheets")
public class TimesheetController {

    @Autowired
    private TimesheetService timesheetService;

    @PostMapping("/submit")
    public ResponseEntity<?> submitTimesheet(@RequestBody TimesheetRequest request) {
        if (request.getEmployeeId() == null || request.getWeekStartDate() == null || request.getTasks() == null) {
            return ResponseEntity.badRequest().body("Invalid request payload");
        }

        System.out.println("Received request: " + request);

        // Adjust the weekStartDate to the nearest Monday
        Date adjustedWeekStartDate = request.getWeekStartDate();
        request.setWeekStartDate(adjustedWeekStartDate);

        // Adjust dates in tasks' dateHoursMap
        request.getTasks().forEach(task -> {
            Map<String, Integer> adjustedDateHoursMap = task.getDateHoursMap();
            task.setDateHoursMap(adjustedDateHoursMap);
        });
        System.out.println("Adjusted Week Start Date: " + adjustedWeekStartDate);
        System.out.println("Adjusted Tasks: " + request.getTasks());


        Timesheet timesheet = timesheetService.createOrUpdateTimesheet(
                request.getEmployeeId(),
                adjustedWeekStartDate,
                request.getTasks()
        );

        return ResponseEntity.ok("Timesheet submitted successfully!");
    }

    // Helper method to adjust a Date to the nearest Monday
    private Date adjustToMonday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek != Calendar.MONDAY) {
            int daysToMonday = (Calendar.MONDAY - dayOfWeek + 7) % 7; // Days to next Monday
            calendar.add(Calendar.DAY_OF_MONTH, daysToMonday);
        }

        return calendar.getTime();
    }

    // Helper method to shift dates in the dateHoursMap by one day forward
    private Map<String, Integer> shiftDatesByOneDay(Map<String, Integer> dateHoursMap) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Integer> shiftedMap = new HashMap<>();

        dateHoursMap.forEach((dateString, hours) -> {
            try {
                Date date = dateFormat.parse(dateString);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, 1); // Shift date by one day
                String shiftedDate = dateFormat.format(calendar.getTime());
                shiftedMap.put(shiftedDate, hours);
            } catch (Exception e) {
                throw new RuntimeException("Error parsing date: " + dateString, e);
            }
        });

        return shiftedMap;
    }


    @GetMapping("/{employeeId}/{weekStartDate}")
    public ResponseEntity<Timesheet> getTimesheet(
            @PathVariable Integer employeeId,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date weekStartDate) {
        Timesheet timesheet = timesheetService.getTimesheet(employeeId, weekStartDate);
        return ResponseEntity.ok(timesheet);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateTimesheet(@RequestBody TimesheetRequest updatedTimesheet) {
        try {
            // Fetch the existing timesheet from the database
            Timesheet existingTimesheet = timesheetService.getTimesheet(
                    updatedTimesheet.getEmployeeId(),
                    updatedTimesheet.getWeekStartDate()
            );

            if (existingTimesheet == null) {
                return ResponseEntity.status(404).body("Timesheet not found for the given employee and week.");
            }

            // Clear existing tasks
            existingTimesheet.getTasks().clear();

            // Add updated tasks with proper parent relationship
            for (Task updatedTask : updatedTimesheet.getTasks()) {
                updatedTask.setTimesheet(existingTimesheet); // Link each task to the timesheet
                existingTimesheet.getTasks().add(updatedTask); // Add the task to the timesheet
            }

            // Save the updated timesheet
            timesheetService.saveTimesheet(existingTimesheet);

            return ResponseEntity.ok("Timesheet updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while updating the timesheet: " + e.getMessage());
        }
    }

    @PutMapping("/{timesheetId}/approval")
    public Timesheet updateApprovalStatus(
            @PathVariable Integer timesheetId,
            @RequestParam String approvalStatus
    ) {
        return timesheetService.updateApprovalStatus(timesheetId, approvalStatus);
    }


    @GetMapping("/pending")
    public List<Timesheet> getPendingTimesheets() {
        return timesheetService.getPendingTimesheets();
    }

}
