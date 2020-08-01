package com.thoughtworks.springbootemployee;

import com.thoughtworks.springbootemployee.dao.EmployeeRepository;
import com.thoughtworks.springbootemployee.dto.EmployeeRequest;
import com.thoughtworks.springbootemployee.dto.EmployeeResponse;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {
    EmployeeRepository mockedEmployeeRepository = mock(EmployeeRepository.class);
    EmployeeService employeeService = new EmployeeService(mockedEmployeeRepository);

    @Test
    void should_return_employees_list_when_getAllEmployees() {
        //when
        given(mockedEmployeeRepository.findAll()).willReturn(getMockEmployeeListData());
        List<EmployeeResponse> actualEmployees = employeeService.getAllEmployees();
        //then
        assertEquals(3, actualEmployees.size());
    }

    @Test
    void should_return_employee_when_get_employ_by_id_given_employee_id() {
        //given
        Employee employee = new Employee(1, "mandy", 18, "female", 99999);
        given(mockedEmployeeRepository.findById(1)).willReturn(Optional.of(employee));
        //when
        EmployeeResponse actualEmploy = employeeService.getEmployeeById(1);
        //then
        assertEquals(Optional.of(employee).get().getId(), actualEmploy.getId());
    }

    @Test
    void should_return_employee_list_when_getAllEmployees_given_page_and_pageSize() {
        //given
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1, "mandy", 18, "female", 99999));
        employees.add(new Employee(2, "Austin", 18, "male", 99999));
        given(mockedEmployeeRepository.findAll(PageRequest.of(1, 2))).willReturn(new PageImpl<Employee>(employees));
        //when
        Page<Employee> actualEmployees = employeeService.getPageEmployees(1, 2);
        //then
        assertEquals(new PageImpl<Employee>(employees), actualEmployees);
    }

    @Test
    void should_return_all_male_employees_when_getAllEmployees_given_gender_is_male() {
        //given
        given(mockedEmployeeRepository.findAllByGender("male")).willReturn(getMockEmployeeListData());
        //when
        List<EmployeeResponse> actualEmployees = employeeService.getEmployeesByGender("male");
        //then
        assertEquals(getMockEmployeeListData().size(), actualEmployees.size());
    }

    @Test
    void should_return_employ_when_add_employee_given_employee() {
        //given
        Employee employee = new Employee(1, "mandy", 18, "female", 99999, 1);
        EmployeeRequest request = new EmployeeRequest(1, "mandy", 18, "female", 99999, 1);
        given(mockedEmployeeRepository.save(isA(Employee.class))).willReturn(employee);
        //when
        EmployeeResponse actualEmployee = employeeService.addEmployee(request);
        //then
        assertEquals(employee.getId(), actualEmployee.getId());
    }

    @Test
    void should_return_employee_when_update_employee_given_employee() {
        //given
        Employee employee = new Employee(1, "mandy", 18, "female", 66666);
        Employee updateEmployee = new Employee(1, "mandy", 19, "female", 66666);
        EmployeeRequest employeeRequest = new EmployeeRequest(1, "mandy", 19, "female", 66666);
        given(mockedEmployeeRepository.findById(eq(1))).willReturn(Optional.of(employee));
        given(mockedEmployeeRepository.save(isA(Employee.class))).willReturn(updateEmployee);
        //when
        EmployeeResponse actualEmployee = employeeService.updateEmployee(1, employeeRequest);
        //the
        assertEquals(updateEmployee.getAge(), actualEmployee.getAge());
    }

    @Test
    void should_return_nothing_when_delete_employee_given_employeeId() {
        //given
        given(mockedEmployeeRepository.findById(1)).willReturn(Optional.of(getMockEmployeeData()));
        //when
        employeeService.deleteEmployeeById(1);
        //then
        verify(mockedEmployeeRepository).deleteById(1);
    }

    private List<Employee> getMockEmployeeListData() {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1, "mandy", 18, "female", 99999));
        employees.add(new Employee(2, "Austin", 18, "male", 99999));
        employees.add(new Employee(3, "other", 18, "male", 99999));
        return employees;
    }

    private Employee getMockEmployeeData() {
        return new Employee(1, "mandy", 18, "female", 66666);
    }
}
