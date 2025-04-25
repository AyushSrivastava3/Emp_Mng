package com.dq.empportal.service

import spock.lang.Specification
import spock.lang.Subject
import com.dq.empportal.model.Employee
import com.dq.empportal.repository.ClientRepository
import com.dq.empportal.repository.EmployeeRepository

class EmployeeServiceSpec extends Specification {
    @Subject
    EmployeeService employeeService

    EmployeeRepository employeeRepository = Mock()
    ClientRepository clientRepository = Mock()

    def setup() {
        employeeService = new EmployeeService(employeeRepository, clientRepository)
    }

    def "test getAllEmployees"() {
        given:
        def employees = [new Employee(), new Employee()]
        employeeRepository.findAll() >> employees

        when:
        def result = employeeService.getAllEmployees()

        then:
        result.size() == 2
        1 * employeeRepository.findAll()
    }
}

