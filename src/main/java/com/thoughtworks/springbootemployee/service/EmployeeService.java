package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.dao.EmployeeRepository;
import com.thoughtworks.springbootemployee.dto.EmployeeRequest;
import com.thoughtworks.springbootemployee.dto.EmployeeResponse;
import com.thoughtworks.springbootemployee.exception.NoSuchDataException;
import com.thoughtworks.springbootemployee.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.thoughtworks.springbootemployee.mapper.EmployeeMapper.toEmployee;
import static com.thoughtworks.springbootemployee.mapper.EmployeeMapper.toEmployeeResponse;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeResponse getEmployeeById(int employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        checkEmployeeModel(employee);
        return toEmployeeResponse(employee);
    }

    public EmployeeResponse addEmployee(EmployeeRequest employeeRequest) {
        Employee employee = toEmployee(employeeRequest);
        return toEmployeeResponse(employeeRepository.save(employee));
    }

    public EmployeeResponse updateEmployee(int employeeId, EmployeeRequest employeeRequest) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        checkEmployeeModel(employee);
        employee.setAge(employeeRequest.getAge());
        employee.setGender(employeeRequest.getGender());
        employee.setName(employeeRequest.getName());
        employee.setSalary(employeeRequest.getSalary());
        return toEmployeeResponse(employeeRepository.save(employee));
    }

    public void deleteEmployeeById(int employeeId) {
        employeeRepository.deleteById(employeeId);
    }

    public List<EmployeeResponse> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeResponse> employeeResponses = new ArrayList<>();
        employees.forEach(employee -> employeeResponses.add(toEmployeeResponse(employee)));
        return employeeResponses;
    }

    public List<EmployeeResponse> getEmployeesByGender(String gender) {
        List<Employee> employees = employeeRepository.findAllByGender(gender);
        List<EmployeeResponse> employeeResponses = new ArrayList<>();
        employees.forEach(employee -> employeeResponses.add(toEmployeeResponse(employee)));
        return employeeResponses;
    }

    public Page<Employee> getPageEmployees(int page, int pageSize) {
        return employeeRepository.findAll(PageRequest.of(page, pageSize));
    }

    private void checkEmployeeModel(Employee employee) {
        if (employee == null) {
            throw new NoSuchDataException();
        }
    }
}
