package com.dq.empportal.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "timesheet_id", nullable = false)
    @JsonBackReference
    private Timesheet timesheet;

    private String taskDescription; // Description of the task
    @ElementCollection
    @CollectionTable(name = "task_date_hours", joinColumns = @JoinColumn(name = "task_id"))
    @MapKeyColumn(name = "date") // Column name for the key (String)
    @Column(name = "hours") // Column name for the value (Integer)
    private Map<String, Integer> dateHoursMap = new HashMap<>();

}
