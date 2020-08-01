package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.dao.CompanyRepository;
import com.thoughtworks.springbootemployee.dto.CompanyRequest;
import com.thoughtworks.springbootemployee.dto.CompanyResponse;
import com.thoughtworks.springbootemployee.dto.EmployeeResponse;
import com.thoughtworks.springbootemployee.exception.IllegalOperationException;
import com.thoughtworks.springbootemployee.exception.NoSuchDataException;
import com.thoughtworks.springbootemployee.mapper.CompanyMapper;
import com.thoughtworks.springbootemployee.mapper.EmployeeMapper;
import com.thoughtworks.springbootemployee.model.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.thoughtworks.springbootemployee.mapper.CompanyMapper.toCompany;
import static com.thoughtworks.springbootemployee.mapper.CompanyMapper.toCompanyResponse;
import static com.thoughtworks.springbootemployee.mapper.EmployeeMapper.toEmployeeResponse;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;



    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<CompanyResponse> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        List<CompanyResponse> companyResponses = new ArrayList<>();
        companies.forEach(company -> companyResponses.add(toCompanyResponse(company)));
        return companyResponses;
    }

    public Page<Company> getAllCompanies(Integer page, Integer pageSize) {
        return companyRepository.findAll(PageRequest.of(page - 1, pageSize));
    }

    public CompanyResponse getCompanyById(int companyId) {
        Company company = companyRepository.findById(companyId).orElse(null);
        if (company == null) {
            throw new NoSuchDataException();
        }
        return toCompanyResponse(company);
    }

    public List<EmployeeResponse> getAllEmployeeOfCompany(int companyId) {
        Company company = companyRepository.findById(companyId).orElse(null);
        if (company == null) {
            throw new NoSuchDataException();
        }
        List<EmployeeResponse> employeeResponses = new ArrayList<>();
        company.getEmployees().forEach(employee -> employeeResponses.add(toEmployeeResponse(employee)));
        return employeeResponses;
    }


    public CompanyResponse addCompany(CompanyRequest companyRequest) {
        Company company = toCompany(companyRequest);
        checkCompanyModel(company);

        return toCompanyResponse(companyRepository.save(company));
    }

    public CompanyResponse updateCompany(Integer companyId, CompanyRequest companyRequest) {
        Company company = toCompany(companyRequest);
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
        return toCompanyResponse(companyRepository.save(company));

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
