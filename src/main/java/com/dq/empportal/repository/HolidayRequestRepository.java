package com.dq.empportal.repository;

import com.dq.empportal.model.HolidayRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HolidayRequestRepository extends JpaRepository<HolidayRequest,Long> {
    List<HolidayRequest> findByStatus(String status);
}
