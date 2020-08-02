package com.thoughtworks.springbootemployee;

import com.alibaba.fastjson.JSON;
import com.thoughtworks.springbootemployee.dao.CompanyRepository;
import com.thoughtworks.springbootemployee.dao.EmployeeRepository;
import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.util.Lists.emptyList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @BeforeEach
    void tearDown() {
        employeeRepository.deleteAll();
        companyRepository.deleteAll();
    }

    @Test
    void should_return_companies_when_getAllCompanies_given_request() throws Exception {
        //given
        List<Company> companies = companyRepository.saveAll(getMockCompanies());
        //when
        mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(10))
                .andExpect(jsonPath("$.msg").value("succeed"))
                .andExpect(jsonPath("$.data[0].id").value(companies.get(0).getId()));
        //then
    }

    @Test
    void should_return_employees_when_getAllEmployees_given_() throws Exception {
        //given
        Company company = companyRepository.save(new Company(1, "huawei", 100, emptyList()));
        List<Employee> employees = employeeRepository.saveAll(getMockEmployees(company.getId()));
        //when
        mockMvc.perform(get("/companies/" + company.getId() + "/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(10))
                .andExpect(jsonPath("$.msg").value("succeed"))
                .andExpect(jsonPath("$.data[0].name").value(employees.get(0).getName()))
                .andExpect(jsonPath("$.data[0].age").value(employees.get(0).getAge()))
                .andExpect(jsonPath("$.data[0].gender").value(employees.get(0).getGender()))
                .andExpect(jsonPath("$.data[0].salary").value(employees.get(0).getSalary()));

        //then

    }

    @Test
    void should_return_company_when_get_company_by_id_given_company_id() throws Exception {
        List<Company> companies = companyRepository.saveAll(getMockCompanies());
        //given
        mockMvc.perform(get("/companies/" + companies.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(companies.get(0).getId()))
                .andExpect(jsonPath("$.data.employeesNumber").value(companies.get(0).getEmployeesNumber()))
                .andExpect(jsonPath("$.data.companyName").value(companies.get(0).getCompanyName()));
        //when

        //then

    }

    @Test
    void should_return_company_when_add_company_given_company() throws Exception {
        //given
        Company company = companyRepository.save(new Company(1, "alibaba2", 100, emptyList()));
        String json = JSON.toJSONString(company);
        //when
        mockMvc.perform(post("/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(company.getId()))
                .andExpect(jsonPath("$.data.companyName").value(company.getCompanyName()))
                .andExpect(jsonPath("$.data.employeesNumber").value(company.getEmployeesNumber()));
        //then

    }

    @Test
    void should_return_company_when_update_company_given_company() throws Exception {
        //given
        Company company = companyRepository.save(new Company(1, "alibaba2", 100, Collections.emptyList()));
        String json = JSON.toJSONString(company);
        //when
        mockMvc.perform(put("/companies/" + company.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.data.id").value(company.getId()))
                .andExpect(jsonPath("$.data.employeesNumber").value(200))
                .andExpect(jsonPath("$.data.companyName").value("alibaba3"));
        //then
    }

    @Test
    void should_when_delete_company_by_id_given_company_id() throws Exception {
        //given
        Company company = companyRepository.save(new Company(1, "alibaba2", 100, Collections.emptyList()));
        //when
        mockMvc.perform(delete("/companies/" + company.getId())).andExpect(status().isOk());
        //then
        assertFalse(companyRepository.findById(company.getId()).isPresent());
    }

    @Test
    void should_return_company_list_when_get_all_company_after_Pagination_given_page_and_pageSize() throws Exception {
        //given
        List<Company> companies = companyRepository.saveAll(getMockCompanies());
        //when
        mockMvc.perform(get("/companies")
                .param("page", "1")
                .param("pageSize", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.numberOfElements").value(5));
    }

    private List<Company> getMockCompanies() {
        List<Company> companies = new ArrayList<>();
        companies.add(new Company(1, "Mm", 10, null));
        companies.add(new Company(2, "Aa", 10, null));
        companies.add(new Company(3, "Bb", 10, null));
        companies.add(new Company(4, "Cc", 10, null));
        companies.add(new Company(5, "Dd", 10, null));
        companies.add(new Company(6, "Ff", 10, null));
        companies.add(new Company(7, "Gg", 10, null));
        companies.add(new Company(8, "Ll", 10, null));
        companies.add(new Company(9, "Pp", 10, null));
        companies.add(new Company(10, "Uu", 10, null));
        return companies;
    }

    private List<Employee> getMockEmployees(Integer companyId) {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1, "xiaoyi", 18, "Male", 30000.0, companyId));
        employees.add(new Employee(2, "xiaoer", 18, "Male", 30000.0, companyId));
        employees.add(new Employee(3, "xiaosan", 19, "Male", 30000.0, companyId));
        employees.add(new Employee(4, "xiaosi", 19, "Male", 30000.0, companyId));
        employees.add(new Employee(5, "xiaowu", 20, "Male", 30000.0, companyId));
        employees.add(new Employee(6, "xiaoliu", 20, "Female", 30000.0, companyId));
        employees.add(new Employee(7, "xiaoqi", 21, "Female", 30000.0, companyId));
        employees.add(new Employee(8, "xiaoba", 21, "Female", 30000.0, companyId));
        employees.add(new Employee(9, "xiaojiu", 18, "Male", 30000.0, companyId));
        employees.add(new Employee(10, "xiaoshi", 18, "Male", 30000.0, companyId));
        return employees;
    }

}
