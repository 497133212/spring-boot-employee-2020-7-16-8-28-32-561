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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.thoughtworks.springbootemployee.mapper.CompanyMapper.toCompany;
import static com.thoughtworks.springbootemployee.mapper.CompanyMapper.toCompanyResponse;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAll()
                .stream().map(CompanyMapper::toCompanyResponse)
                .collect(Collectors.toList());
    }

    public Page<Company> getAllCompanies(Integer page, Integer pageSize) {
        return companyRepository.findAll(PageRequest.of(page - 1, pageSize));
    }

    public CompanyResponse getCompanyById(int companyId) {
        return toCompanyResponse(findCompanyById(companyId));
    }

    public List<EmployeeResponse> getAllEmployeeByCompanyId(int companyId) {
        return findCompanyById(companyId)
                .getEmployees().stream()
                .map(EmployeeMapper::toEmployeeResponse)
                .collect(Collectors.toList());
    }

    public CompanyResponse addCompany(CompanyRequest companyRequest) {
        return toCompanyResponse(companyRepository.save(toCompany(companyRequest)));
    }

    public CompanyResponse updateCompany(Integer companyId, CompanyRequest companyRequest) {
        if (companyId.intValue() != companyRequest.getId()) {
            throw new IllegalOperationException();
        }
        Company company = toCompany(companyRequest);
        Company companyInfo = companyRepository.findById(companyId).orElseThrow(NoSuchDataException::new);
        companyInfo.setCompanyName(company.getCompanyName());
        companyInfo.setEmployeesNumber(company.getEmployeesNumber());
        return toCompanyResponse(companyRepository.save(company));
    }

    public void deleteCompanyById(Integer companyId) {
        companyRepository.deleteById(companyId);
    }

    private Company findCompanyById(int companyId) {
        return companyRepository.findById(companyId).orElseThrow(NoSuchDataException::new);
    }

}
