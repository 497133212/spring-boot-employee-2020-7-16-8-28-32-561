package com.thoughtworks.springbootemployee;

import com.thoughtworks.springbootemployee.dao.CompanyRepository;
import com.thoughtworks.springbootemployee.dto.CompanyRequest;
import com.thoughtworks.springbootemployee.dto.CompanyResponse;
import com.thoughtworks.springbootemployee.dto.EmployeeResponse;
import com.thoughtworks.springbootemployee.exception.IllegalOperationException;
import com.thoughtworks.springbootemployee.exception.NoSuchDataException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CompanyServiceTest {
    CompanyRepository mockedCompanyRepository = mock(CompanyRepository.class);
    CompanyService companyService = new CompanyService(mockedCompanyRepository);

    @Test
    void should_return_company_list_when_getAllCompanies() {
        //when
        given(mockedCompanyRepository.findAll()).willReturn(getMockCompanyListData());
        List<CompanyResponse> actualCompanies = companyService.getAllCompanies();
        //then
        assertEquals(2, actualCompanies.size());
    }

    @Test
    void should_return_company_when_get_company_by_id_given_company_id() {
        //given
        Company company = new Company(1, "alibaba", 100, null);
        given(mockedCompanyRepository.findById(1)).willReturn(Optional.of(company));
        //when
        CompanyResponse actualCompany = companyService.getCompanyById(1);
        //then
        assertEquals(company.getId(), actualCompany.getId());
    }

    @Test
    void should_throw_exception_when_get_company_by_id_given_company_id_inexistence() {
        //given
        given(mockedCompanyRepository.findById(100)).willReturn(Optional.ofNullable(null));
        //when
        //then
        assertThrows(NoSuchDataException.class, () -> companyService.getCompanyById(100));
    }

    @Test
    void should_return_all_employee_of_company_when_get_all_employee_given_company_id() {
        //given
        given(mockedCompanyRepository.findById(1)).willReturn(Optional.of(getMockCompanyData()));
        //when
        List<EmployeeResponse> actualEmployees = companyService.getAllEmployeeByCompanyId(1);
        //then
        assertEquals(getMockCompanyData().getEmployees().size(), actualEmployees.size());
    }

    @Test
    void should_throw_exception_when_get_all_employee_given_company_id_inexistence() {
        //given
        given(mockedCompanyRepository.findById(1)).willReturn(Optional.ofNullable(null));
        //when
        //then
        assertThrows(NoSuchDataException.class, () -> companyService.getAllEmployeeByCompanyId(1));
    }

    @Test
    void should_return_company_list_when_getAllCompanies_given_page_and_pageSize() {
        //given
        given(mockedCompanyRepository.findAll(isA(PageRequest.class))).willReturn(new PageImpl<Company>(getMockCompanyListData()));
        //when
        List<Company> actualCompanies = companyService.getAllCompanies(1, 2).toList();
        //then
        assertEquals(getMockCompanyListData().size(), actualCompanies.size());
    }

    @Test
    void should_return_company_when_add_company_given_company() {
        //given
        CompanyRequest companyRequest = new CompanyRequest(1, "alibaba", 200, null);
        given(mockedCompanyRepository.save(isA(Company.class))).willReturn(new Company(1, "alibaba", 200, null));
        //when
        CompanyResponse actualCompanyResponse = companyService.addCompany(companyRequest);
        //then
        assertEquals("alibaba", actualCompanyResponse.getCompanyName());
    }

    @Test
    void should_return_company_when_update_company_given_company_Id_and_company() {
        //given
        Company company = new Company(1, "alibaba", 200, null);
        CompanyRequest updateCompany = new CompanyRequest(1, "xiaomi", 100, null);
        Company updatedCompany = new Company(1, "xiaomi", 100, null);
        given(mockedCompanyRepository.findById(1)).willReturn(Optional.of(company));
        given(mockedCompanyRepository.save(isA(Company.class))).willReturn(updatedCompany);
        //when
        CompanyResponse actualCompany = companyService.updateCompany(1, updateCompany);
        //then
        verify(mockedCompanyRepository).save(isA(Company.class));
        assertEquals(updateCompany.getCompanyName(), actualCompany.getCompanyName());
    }

    @Test
    void should_throw_no_such_data_exception_when_update_company_given_company_Id_and_company() {
        //given
        CompanyRequest updateCompany = new CompanyRequest(1, "xiaomi", 100, null);
        given(mockedCompanyRepository.findById(1)).willReturn(Optional.ofNullable(null));
        //when
        //then
        assertThrows(NoSuchDataException.class, () -> companyService.updateCompany(1, updateCompany));
    }

    @Test
    void should_throw_illegal_exception_update_company_given_company_Id_and_company_no_equal() {
        //given
        Company company = new Company(1, "alibaba", 200, null);
        CompanyRequest updateCompany = new CompanyRequest(2, "xiaomi", 100, null);
        //when
        assertThrows(IllegalOperationException.class, () -> companyService.updateCompany(1, updateCompany));
    }

    @Test
    void should_delete_all_employees_belong_to_company_when_delete_employees_of_company_by_id_given_company_id() {
        //given
        //when
        companyService.deleteCompanyById(1);
        //then
        verify(mockedCompanyRepository).deleteById(eq(1));
    }

    private List<Company> getMockCompanyListData() {
        List<Company> companies = new ArrayList<>();
        companies.add(new Company(1, "alibaba", 100, null));
        companies.add(new Company(2, "oocl", 100, null));
        return companies;
    }

    private Company getMockCompanyData() {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1, "alibaba1", 20, "male", 6000));
        employees.add(new Employee(2, "alibaba2", 20, "female", 6000));
        Company company = new Company(1, "alibaba", 100, employees);
        return company;
    }
}
