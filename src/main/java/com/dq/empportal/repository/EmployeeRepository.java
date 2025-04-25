package com.dq.empportal.repository;

import com.dq.empportal.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer> {
//    @Query("SELECT e FROM Employee e WHERE e.dateOfJoining <= :endDate " +
//            "AND (e.dateOfResigning IS NULL OR e.dateOfResigning >= :startDate)")
//    List<Employee> findActiveEmployeesInMonth(@Param("startDate") LocalDate startDate,
//                                              @Param("endDate") LocalDate endDate);
//    Optional<Employee> findByProfessionalEmail(String professionalEmail);




    // Call stored procedure for active employees in a month
  @Transactional
    @Procedure(procedureName = "find_active_employees_in_month")
    List<Employee> getActiveEmployeesInMonth(@Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate);

    // Call stored procedure to find employee by email
    @Transactional
    @Procedure(procedureName = "find_employee_by_email")
    Optional<Employee> findByProfessionalEmail(@Param("email") String professionalEmail);

}
