package com.dq.empportal.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Timesheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer employeeId;
    @Temporal(TemporalType.DATE)
    private Date weekStartDate; // Start date of the week

    @OneToMany(mappedBy = "timesheet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Task> tasks = new ArrayList<>();

    // Audit fields
    private boolean submitted; // Whether the timesheet is submitted
    private LocalDateTime submittedAt;
    // Approval status field
    private String approvalStatus; // Pending, Approved, or Rejected

    @PrePersist
    private void setDefaultApprovalStatus() {
        if (approvalStatus == null) {
            approvalStatus = "Pending"; // Default to Pending
        }
    }
}
