package com.dq.empportal.repository;

import com.dq.empportal.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer> {
    @Query("SELECT e FROM Employee e WHERE e.dateOfJoining <= :endDate " +
            "AND (e.dateOfResigning IS NULL OR e.dateOfResigning >= :startDate)")
    List<Employee> findActiveEmployeesInMonth(@Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate);
    Optional<Employee> findByProfessionalEmail(String professionalEmail);

}
