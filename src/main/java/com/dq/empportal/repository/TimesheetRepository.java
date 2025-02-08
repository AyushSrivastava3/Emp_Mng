package com.dq.empportal.repository;

import com.dq.empportal.model.Employee;
import com.dq.empportal.model.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet,Integer> {
    List<Timesheet> findByWeekStartDateBetween(Date startDate, Date endDate);

    Optional<Timesheet> findByEmployeeIdAndWeekStartDate(Integer employeeId, Date weekStartDate);
    List<Timesheet> findByApprovalStatus(String approvalStatus);
}
