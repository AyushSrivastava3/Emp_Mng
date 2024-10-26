package com.employee_management_system.Employee_Management.Dtos;

import com.employee_management_system.Employee_Management.Model.Employee;
import com.employee_management_system.Employee_Management.Model.EmployeeClientInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InvoiceResponseDto {
    private Integer id;
    private Integer clientId;
    private String clientName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double totalAmount; // Total amount for the invoice
    private String status;
    private List<EmployeeClientInfo> employees;
}
