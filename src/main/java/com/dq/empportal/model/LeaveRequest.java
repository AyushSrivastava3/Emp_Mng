package com.dq.empportal.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LeaveRequest {
    private int employeeId; // Reference to EmployeeClientInfo
    private String employeeName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String type;  // Example: "Paid", "Unpaid", etc.
    private String reason;
}
