package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.dao.CompanyRepository;
import com.thoughtworks.springbootemployee.dto.CompanyRequest;
import com.thoughtworks.springbootemployee.exception.IllegalOperationException;
import com.thoughtworks.springbootemployee.exception.NoSuchDataException;
import com.thoughtworks.springbootemployee.mapper.CompanyMapper;
import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import org.springframework.data.domain.Page;
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

        Company company = companyRepository.findById(companyId).orElse(null);
        if (company == null) {
            throw new NoSuchDataException();
        }
        return company;
    }

    public List<Employee> getAllEmployeeOfCompany(int companyId) {
        Company company = companyRepository.findById(companyId).orElse(null);
        if (company == null) {
            throw new NoSuchDataException();
        }
        return company.getEmployees();
    }

    public List<Company> getAllCompanies() {

        return companyRepository.findAll();
    }

    public Page<Company> getAllCompanies(Integer page, Integer pageSize) {

        return companyRepository.findAll(PageRequest.of(page - 1, pageSize));
    }

    public Company addCompany(CompanyRequest companyRequest) {
        Company company = companyMapper.toCompany(companyRequest);
        checkCompanyModel(company);

        return companyRepository.save(company);
    }

    public Company updateCompany(Integer companyId, CompanyRequest companyRequest) {
        Company company = companyMapper.toCompany(companyRequest);
        checkCompanyModel(company);
        Optional<Company> optionalCompany = companyRepository.findById(companyId);
        if (!optionalCompany.isPresent()) {
            throw new NoSuchDataException();
        }

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

    private void checkCompanyModel(Company company) {
        if (Objects.isNull(company)) {
            throw new IllegalOperationException();
        }
    }

    public void deleteCompanyById(Integer companyId) {
        if (Objects.nonNull(companyId)) {
            companyRepository.deleteById(companyId);
        }
    }
}
