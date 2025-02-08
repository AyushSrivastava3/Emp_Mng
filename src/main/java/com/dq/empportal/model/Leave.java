package com.dq.empportal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "employee_leave")
public class Leave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private String employeeName;

    @Column(nullable = false)
    private Integer employeeId;

    private LocalDate startDate;
    private LocalDate endDate;
    private String type;
    private String reason;
    private String status; // "Pending", "Approved", "Rejected"
    private String rejectionReason;
    private String approvedBy; // Approver's name or ID
    private LocalDate approvalDate;

    // Compute the number of days for the leave
    public int getLeaveDuration() {
        if (startDate == null || endDate == null) {
            throw new IllegalStateException("Start date and end date must not be null to calculate leave duration.");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }
        return Period.between(startDate, endDate).getDays() + 1; // Including both start and end date
    }
}


