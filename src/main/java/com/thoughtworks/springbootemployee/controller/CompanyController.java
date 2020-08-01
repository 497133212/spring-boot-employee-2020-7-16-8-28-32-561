package com.thoughtworks.springbootemployee.controller;

import com.thoughtworks.springbootemployee.common.JsonResult;
import com.thoughtworks.springbootemployee.dto.CompanyRequest;
import com.thoughtworks.springbootemployee.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.thoughtworks.springbootemployee.common.JsonResult.success;


@RestController
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    CompanyService companyService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{companyId}")
    public JsonResult getCompanyByCompanyId(@PathVariable int companyId) {
        return success(companyService.getCompanyById(companyId));
    }

    @GetMapping("/{companyId}/employees")
    @ResponseStatus(HttpStatus.OK)
    public JsonResult getAllEmployees(@PathVariable int companyId) {
        return success(companyService.getAllEmployeeByCompanyId(companyId));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public JsonResult getAllCompanies() {
        return success(companyService.getAllCompanies());
    }

    @GetMapping(params = {"page", "pageSize"})
    @ResponseStatus(HttpStatus.OK)
    public JsonResult getAllCompanies(@RequestParam Integer page, @RequestParam Integer pageSize) {
        return success(companyService.getAllCompanies(page, pageSize));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JsonResult addCompany(@RequestBody CompanyRequest companyRequest) {
        return success(companyService.addCompany(companyRequest));
    }

    @PutMapping("/{companyId}")
    @ResponseStatus(HttpStatus.OK)
    public JsonResult updateCompany(@PathVariable Integer companyId, @RequestBody CompanyRequest companyRequest) {
        return success(companyService.updateCompany(companyId, companyRequest));
    }

    @DeleteMapping("/{companyId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCompanyById(@PathVariable Integer companyId) {
        companyService.deleteCompanyById(companyId);
    }
}
