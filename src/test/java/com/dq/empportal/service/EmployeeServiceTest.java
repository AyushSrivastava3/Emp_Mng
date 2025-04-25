//package com.dq.empportal.service;
//
//
//import com.dq.empportal.model.Employee;
//import com.dq.empportal.repository.ClientRepository;
//import com.dq.empportal.repository.EmployeeRepository;
//import jakarta.mail.MessagingException;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDate;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class EmployeeServiceTest {
//    @InjectMocks
//    private EmployeeService employeeService;
//
//    @Mock
//    private EmployeeRepository employeeRepository;
//
//    @Mock
//    private ClientRepository clientRepository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetAllEmployees() {
//        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
//        when(employeeRepository.findAll()).thenReturn(employees);
//
//        List<Employee> result = employeeService.getAllEmployees();
//
//        assertEquals(2, result.size());
//        verify(employeeRepository, times(1)).findAll();
//    }
//
//    @Test
//    void testGetEmployeeById() {
//        Employee employee = new Employee();
//        employee.setId(1);
//        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
//
//        Optional<Employee> result = employeeService.getEmployeeById(1);
//
//        assertTrue(result.isPresent());
//        assertEquals(1, result.get().getId());
//        verify(employeeRepository, times(1)).findById(1);
//    }
//
//    @Test
//    void testCreateEmployee() throws MessagingException {
//        Employee employee = new Employee();
//        when(employeeRepository.save(employee)).thenReturn(employee);
//
//        Employee result = employeeService.createEmployee(employee);
//
//        assertNotNull(result);
//        verify(employeeRepository, times(1)).save(employee);
//    }
//
//    @Test
//    void testUpdateEmployee() {
//        Employee existingEmployee = new Employee();
//        existingEmployee.setId(1);
//
//        Employee updatedDetails = new Employee();
//        updatedDetails.setFirstName("John");
//
//        when(employeeRepository.findById(1)).thenReturn(Optional.of(existingEmployee));
//        when(employeeRepository.save(existingEmployee)).thenReturn(existingEmployee);
//
//        Employee result = employeeService.updateEmployee(1, updatedDetails);
//
//        assertNotNull(result);
//        assertEquals("John", result.getFirstName());
//        verify(employeeRepository, times(1)).findById(1);
//        verify(employeeRepository, times(1)).save(existingEmployee);
//    }
//
//    @Test
//    void testDeleteEmployee() {
//        doNothing().when(employeeRepository).deleteById(1);
//
//        employeeService.deleteEmployee(1);
//
//        verify(employeeRepository, times(1)).deleteById(1);
//    }
//
//
//}