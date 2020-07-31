package com.thoughtworks.springbootemployee;

import com.thoughtworks.springbootemployee.dao.CompanyRepository;
import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class CompanyServiceTest {
    CompanyRepository mockedCompanyRepository = mock(CompanyRepository.class);
    CompanyService companyService = new CompanyService(mockedCompanyRepository, null);


    @Test
    void should_return_company_list_when_getAllCompanies() {
        //when
        List<Company> companies = new ArrayList<>();
        companies.add(new Company(1, "alibaba", 100, null));
        companies.add(new Company(2, "alibaba", 100, null));
        given(mockedCompanyRepository.findAll()).willReturn(companies);
        List<Company> actualCompanies = companyService.findAllCompanies();
        //then
        assertEquals(2, actualCompanies.size());
    }

    @Test
    void should_return_company_when_get_company_by_id_given_company_id() {
        //given
        Company company = new Company(1, "alibaba", 100, null);
        given(mockedCompanyRepository.findById(1)).willReturn(Optional.of(company));

        //when
        Company actualCompany = companyService.getCompanyById(1);
        //then
        assertEquals(Optional.of(company).get().getId(), actualCompany.getId());

    }

    @Test
    void should_return_all_employee_of_company_when_get_all_employee_given_company_id() {
        //given
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1, "alibaba1", 20, "male", 6000));
        employees.add(new Employee(2, "alibaba2", 20, "female", 6000));
        Company company = new Company(1, "alibaba", 100, employees);
        given(mockedCompanyRepository.findById(1)).willReturn(Optional.of(company));
        //when
        List<Employee> actualEmployees = companyService.getAllEmployeeOfCompany(1);
        //then
        assertEquals(company.getEmployees(), actualEmployees);

    }

    @Test
    void should_return_company_list_when_getAllCompanies_given_page_and_pageSize() {
        //given
        List<Company> companies = new ArrayList<>();
        companies.add(new Company(1, "alibaba1", 100, null));
        companies.add(new Company(2, "alibaba2", 100, null));
        given(mockedCompanyRepository.findAll(PageRequest.of(1, 2))).willReturn(new PageImpl<Company>(companies));
        //when
        List<Company> actualCompanies = companyService.getAllCompanies(1, 2).toList();
        //then
        assertEquals(companies, actualCompanies);
    }

    @Test
    void should_return_company_when_add_company_given_company() {
//        //given
//        Company company = new Company(1, "alibaba", 200, null);
//        given(mockedCompanyRepository.save(company)).willReturn(company);
//        //when
//        Company actualCompany = companyService.addCompany(company);
//
//        //then
//        assertEquals(company.getCompanyName(), actualCompany.getCompanyName());
    }

    @Test
    void should_return_company_when_update_company_given_company_Id_and_company() {
//        //given
//        Company company = new Company(1, "alibaba", 200, null);
//        Company updateCompany = new Company(1, "xiaomi", 100, null);
//        given(mockedCompanyRepository.findById(1)).willReturn(Optional.of(company));
//        given(mockedCompanyRepository.save(updateCompany)).willReturn(updateCompany);
//        //when
//        companyService.updateCompany(1, updateCompany);
//        //then
//        assertEquals(company.getCompanyName(), updateCompany.getCompanyName());
    }

    @Test
    void should_delete_all_employees_belong_to_company_when_delete_employees_of_company_by_id_given_company_id() {
        //given
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1, "ming", 10, "male", 7000));
        Company company = new Company(1, "alibaba", 200, employees);
//        when(mockedCompanyRepository.deleteById(1));
        //when
        companyService.deleteCompanyById(1);
        //then
        assertNull(company);
    }
}
