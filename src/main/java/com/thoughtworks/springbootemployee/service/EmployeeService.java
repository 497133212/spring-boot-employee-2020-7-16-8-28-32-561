package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.dao.EmployeeRepository;
import com.thoughtworks.springbootemployee.dto.EmployeeRequest;
import com.thoughtworks.springbootemployee.dto.EmployeeResponse;
import com.thoughtworks.springbootemployee.mapper.EmployeeMapper;
import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;
    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository=employeeRepository;
        this.employeeMapper = employeeMapper;
    }
    public Employee getEmployeeById(int employeeId) {
        return  employeeRepository.findById(employeeId).get();
    }
    public EmployeeResponse addEmployee(EmployeeRequest employeeRequest) {
        Employee employee = employeeMapper.toEmployee(employeeRequest);
        return employeeMapper.toEmployeeResponse(employeeRepository.save(employee));
    }
    public EmployeeResponse updateEmployee(int employeeId, EmployeeRequest employeeRequest) {
        Employee employee =employeeRepository.findById(employeeId).orElse(null);
        if (employee==null){
            return null;
        }else {
            employee.setAge(employeeRequest.getAge());
            employee.setGender(employeeRequest.getGender());
            employee.setName(employeeRequest.getName());
            employee.setSalary(employeeRequest.getSalary());
            return employeeMapper.toEmployeeResponse(employeeRepository.save(employee));
        }
    }
    public void deleteEmployeeById(int employeeId) {
        employeeRepository.deleteById(employeeId);
    }
    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    public List<Employee> getEmployeeByGender(String gender) {
        return employeeRepository.findAllByGender(gender);
    }

    public Page<Employee> getAllEmployees(int page, int pageSize) {
        return employeeRepository.findAll(PageRequest.of(page, pageSize));
    }
}
