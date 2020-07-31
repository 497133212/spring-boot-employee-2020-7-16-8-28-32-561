package com.thoughtworks.springbootemployee.controller;

import com.thoughtworks.springbootemployee.dto.EmployeeRequest;
import com.thoughtworks.springbootemployee.dto.EmployeeResponse;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @GetMapping
    public Page<Employee> getEmployees(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "0") int pageSize, @RequestParam(required = false, defaultValue = "0") String gender) {
        return employeeService.getAllEmployees(page, pageSize);
    }

    @GetMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public Employee getEmployeeById(@PathVariable int employeeId) {
        return employeeService.getEmployeeById(employeeId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponse addEmployee(@RequestBody EmployeeRequest employeeRequest) {
        return employeeService.addEmployee(employeeRequest);
    }

    @PutMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeResponse updateEmployee(@PathVariable int employeeId, @RequestBody EmployeeRequest employeeRequest) {
        return employeeService.updateEmployee(employeeId, employeeRequest);
    }

    @DeleteMapping("{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEmployeeById(@PathVariable int employeeId) {
        employeeService.deleteEmployeeById(employeeId);
    }
}
