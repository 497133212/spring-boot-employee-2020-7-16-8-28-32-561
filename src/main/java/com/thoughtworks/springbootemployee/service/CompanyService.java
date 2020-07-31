package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.dao.CompanyRepository;
import com.thoughtworks.springbootemployee.dto.CompanyRequest;
import com.thoughtworks.springbootemployee.mapper.CompanyMapper;
import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    private final CompanyMapper companyMapper;


    public CompanyService(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
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

    public Company addCompany(CompanyRequest companyRequest) {
        Company company = companyMapper.toCompany(companyRequest);
        if (Objects.nonNull(company)) {
            return companyRepository.save(company);
        }
        return null;
    }

    public Company updateCompany(Integer companyId, CompanyRequest companyRequest) {
        Company company = companyMapper.toCompany(companyRequest);
        if (company != null) {
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

    public void deleteCompanyById(Integer companyId) {
        if (Objects.nonNull(companyId)) {
            companyRepository.deleteById(companyId);
        }
    }
}
