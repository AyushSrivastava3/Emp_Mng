package com.dq.empportal.dtos;

import com.dq.empportal.model.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TimesheetRequest {
    private Integer employeeId;
    private Date weekStartDate;
    private List<Task> tasks;

    // Getters and Setters
}

