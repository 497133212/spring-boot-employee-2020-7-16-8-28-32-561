package com.thoughtworks.springbootemployee.controller;

import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    CompanyService companyService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{companyId}")
    public Company getCompanyByCompanyId(@PathVariable int companyId) {
        return companyService.getCompanyById(companyId);
    }

    @GetMapping("/{companyId}/employees")
    @ResponseStatus(HttpStatus.OK)
    public List<Employee> getAllEmployees(@PathVariable int companyId) {
        return companyService.getAllEmployeeOfCompany(companyId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Company> getAllCompanies(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
        return companyService.getAllCompanies(page, pageSize);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Company addCompany(@RequestBody Company company) {
        return companyService.addCompany(company);
    }

    @PutMapping("/{companyId}")
    @ResponseStatus(HttpStatus.OK)
    public Company updateCompany(@PathVariable Integer companyId, @RequestBody Company company) {
        return companyService.updateCompany(companyId, company);
    }

    @DeleteMapping("/{companyId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCompanyById(@PathVariable Integer companyId) {
        companyService.deleteCompanyById(companyId);
    }
}
