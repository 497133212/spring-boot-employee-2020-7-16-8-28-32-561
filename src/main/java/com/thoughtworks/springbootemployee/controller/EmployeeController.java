package com.thoughtworks.springbootemployee.controller;

import com.thoughtworks.springbootemployee.common.JsonResult;
import com.thoughtworks.springbootemployee.dto.EmployeeRequest;
import com.thoughtworks.springbootemployee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.thoughtworks.springbootemployee.common.JsonResult.success;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @GetMapping
    public JsonResult getEmployees() {
        return success(employeeService.getAllEmployees());
    }

    @GetMapping(params = {"page", "pageSize"})
    public JsonResult getEmployees(@RequestParam int page, @RequestParam int pageSize) {
        return success(employeeService.getPageEmployees(page, pageSize));
    }

    @GetMapping(params = {"gender"})
    public JsonResult getEmployees(@RequestParam String gender) {
        return success(employeeService.getEmployeesByGender(gender));
    }

    @GetMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public JsonResult getEmployeeById(@PathVariable int employeeId) {
        return success(employeeService.getEmployeeById(employeeId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JsonResult addEmployee(@RequestBody EmployeeRequest employeeRequest) {
        return success(employeeService.addEmployee(employeeRequest));
    }

    @PutMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public JsonResult updateEmployee(@PathVariable int employeeId, @RequestBody EmployeeRequest employeeRequest) {
        return success(employeeService.updateEmployee(employeeId, employeeRequest));
    }

    @DeleteMapping("{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEmployeeById(@PathVariable int employeeId) {
        employeeService.deleteEmployeeById(employeeId);
    }
}
