package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.dao.CompanyRepository;
import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

    public Company getCompanyById(int companyId) {
        return companyRepository.findById(companyId).get();
    }

    public List<Employee> getAllEmployeeOfCompany(int companyId) {
        Company company = companyRepository.findById(companyId).get();
        return company.getEmployees();
    }

    public List<Company> getAllCompanies(Integer page, Integer pageSize) {
        if (page == null || pageSize == null) {
            return companyRepository.findAll();
        }
        return companyRepository.findAll(PageRequest.of(page, pageSize)).toList();
    }

    public Company addCompany(Company company) {
        if (company != null) {
            return companyRepository.save(company);
        }
        return null;
    }

    public Company updateCompany(Integer companyId, Company company) {
        if (company != null && company != null) {
            Optional<Company> optionalCompany = companyRepository.findById(companyId);
            if (optionalCompany.isPresent()) {
                Company companyInfo = optionalCompany.get();
                if (!StringUtils.isEmpty(companyInfo.getCompanyName())) {
                    companyInfo.setCompanyName(company.getCompanyName());
                }
                if (!StringUtils.isEmpty(companyInfo.getEmployeesNumber())) {
                    companyInfo.setEmployeesNumber(company.getEmployeesNumber());
                }
                if (!StringUtils.isEmpty(companyInfo.getEmployees())) {
                    companyInfo.setEmployees(companyInfo.getEmployees());
                }
                return companyRepository.save(companyInfo);
            }
        }
        return null;
    }

    public void deleteEmployeesOfCompanyById(Integer companyId) {
        if (companyId != null) {
            Optional<Company> optionalCompany = companyRepository.findById(companyId);
            if (optionalCompany.isPresent()) {
                Company company = optionalCompany.get();
                company.setEmployees(null);
                companyRepository.save(company);
            }
        }
    }
}
