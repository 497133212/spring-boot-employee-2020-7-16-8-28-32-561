package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.dao.EmployeeRepository;
import com.thoughtworks.springbootemployee.dto.EmployeeRequest;
import com.thoughtworks.springbootemployee.dto.EmployeeResponse;
import com.thoughtworks.springbootemployee.exception.NoSuchDataException;
import com.thoughtworks.springbootemployee.mapper.EmployeeMapper;
import com.thoughtworks.springbootemployee.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.thoughtworks.springbootemployee.mapper.EmployeeMapper.toEmployee;
import static com.thoughtworks.springbootemployee.mapper.EmployeeMapper.toEmployeeResponse;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeResponse getEmployeeById(int employeeId) {
        return toEmployeeResponse(employeeRepository.findById(employeeId).orElseThrow(NoSuchDataException::new));
    }

    public EmployeeResponse addEmployee(EmployeeRequest employeeRequest) {
        return toEmployeeResponse(employeeRepository.save(toEmployee(employeeRequest)));
    }

    public EmployeeResponse updateEmployee(int employeeId, EmployeeRequest employeeRequest) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(NoSuchDataException::new);
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

        return employeeRepository.findAll().stream()
                .map(EmployeeMapper::toEmployeeResponse)
                .collect(Collectors.toList());
    }

    public List<EmployeeResponse> getEmployeesByGender(String gender) {

        return employeeRepository.findAllByGender(gender).stream()
                .map(EmployeeMapper::toEmployeeResponse)
                .collect(Collectors.toList());
    }

    public Page<Employee> getPageEmployees(int page, int pageSize) {
        return employeeRepository.findAll(PageRequest.of(page, pageSize));
    }

}
