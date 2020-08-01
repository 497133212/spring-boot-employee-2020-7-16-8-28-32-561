package com.thoughtworks.springbootemployee.controller;

import com.thoughtworks.springbootemployee.dto.CompanyRequest;
import com.thoughtworks.springbootemployee.dto.CompanyResponse;
import com.thoughtworks.springbootemployee.dto.EmployeeResponse;
import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public CompanyResponse getCompanyByCompanyId(@PathVariable int companyId) {
        return companyService.getCompanyById(companyId);
    }

    @GetMapping("/{companyId}/employees")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeResponse> getAllEmployees(@PathVariable int companyId) {
        return companyService.getAllEmployeeByCompanyId(companyId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompanyResponse> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @GetMapping(params = {"page", "pageSize"})
    @ResponseStatus(HttpStatus.OK)
    public Page<Company> getAllCompanies(@RequestParam Integer page, @RequestParam Integer pageSize) {
        return companyService.getAllCompanies(page, pageSize);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyResponse addCompany(@RequestBody CompanyRequest companyRequest) {
        return companyService.addCompany(companyRequest);
    }

    @PutMapping("/{companyId}")
    @ResponseStatus(HttpStatus.OK)
    public CompanyResponse updateCompany(@PathVariable Integer companyId, @RequestBody CompanyRequest companyRequest) {
        return companyService.updateCompany(companyId, companyRequest);
    }

    @DeleteMapping("/{companyId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCompanyById(@PathVariable Integer companyId) {
        companyService.deleteCompanyById(companyId);
    }
}
