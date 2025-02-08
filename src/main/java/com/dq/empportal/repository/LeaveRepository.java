package com.dq.empportal.repository;

import com.dq.empportal.model.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<Leave,Integer> {
    List<Leave> findByEmployeeId(Integer employeeId);
    @Query("SELECT l FROM Leave l WHERE l.employeeId = :employeeId AND l.startDate <= :endDate AND l.endDate >= :startDate")
    List<Leave> findLeavesWithinDateRange(
            @Param("employeeId") Integer employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
