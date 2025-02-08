package com.dq.empportal.repository;

import com.dq.empportal.model.Client;
import com.dq.empportal.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    Invoice findByClientAndStartDateAndEndDate(Client client, LocalDate startDate, LocalDate endDate);
    @Query("SELECT i.status, COUNT(i) FROM Invoice i GROUP BY i.status")
    List<Object[]> countInvoicesByStatus();
    List<Invoice> findByStatus(String status);
    boolean existsByInvoiceNo(String invoiceNo);
    @Query("SELECT i.invoiceNo FROM Invoice i ORDER BY i.id DESC")
    Optional<String> findLastInvoiceNumber();

    @Query("SELECT MAX(CAST(SUBSTRING(i.invoiceNo, 9, 4) AS long)) FROM Invoice i")
    Optional<Long> findMaxInvoiceSequence();
}