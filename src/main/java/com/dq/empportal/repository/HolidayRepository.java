package com.dq.empportal.repository;

import com.dq.empportal.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    boolean existsByDate(LocalDate date); // Check if a holiday already exists
    List<Holiday> findByDateIn(List<LocalDate> dates); // Get multiple holidays
    Optional<Holiday> findByDate(LocalDate date);
}
