package com.thoughtworks.springbootemployee;

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
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
                .andExpect(jsonPath("$", hasSize(10)))
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].id").value(companies.get(0).getId()));
        //then
    }

    @Test
    void should_return_employees_when_getAllEmployees_given_() throws Exception {
        //given
        Company company = new Company(1, "huawei", 100, emptyList());
        companyRepository.save(company);
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1, "lin", 18, "male", 3000.0, 1));
        employees.add(new Employee(2, "wang", 20, "male", 5000.0, 1));
        employees.add(new Employee(3, "huang", 8, "female", 9000.0, 1));
        employeeRepository.saveAll(employees);
        //when
        mockMvc.perform(get("/companies/1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value(employees.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(employees.get(0).getName()))
                .andExpect(jsonPath("$[0].age").value(employees.get(0).getAge()))
                .andExpect(jsonPath("$[0].gender").value(employees.get(0).getGender()))
                .andExpect(jsonPath("$[0].salary").value(employees.get(0).getSalary()));

        //then

    }

    @Test
    void should_return_company_when_get_company_by_id_given_company_id() throws Exception {
        List<Company> companies = companyRepository.saveAll(getMockCompanies());
        //given
        mockMvc.perform(get("/companies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(companies.get(0).getId()))
                .andExpect(jsonPath("$.employeesNumber").value(companies.get(0).getEmployeesNumber()))
                .andExpect(jsonPath("$.companyName").value(companies.get(0).getCompanyName()));
        //when

        //then

    }

    @Test
    void should_return_company_when_add_company_given_company() throws Exception {
        //given
        Company company = new Company(1, "alibaba2", 100, emptyList());
        companyRepository.save(company);
        //when
        mockMvc.perform(post("/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\n" +
                                "        \"id\": 1,\n" +
                                "        \"companyName\": \"alibaba2\",\n" +
                                "        \"employeesNumber\": 100,\n" +
                                "        \"employees\": [\n" +
                                "        ]\n" +
                                "    }"
                ))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(company.getId()))
                .andExpect(jsonPath("$.companyName").value(company.getCompanyName()))
                .andExpect(jsonPath("$.employeesNumber").value(company.getEmployeesNumber()));
        //then

    }

    @Test
    void should_return_company_when_update_company_given_company() throws Exception {
        //given
        Company company = new Company(1, "alibaba2", 100, Collections.emptyList());
        companyRepository.save(company);
        //when
        mockMvc.perform(put("/companies/1").contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "        \"id\": 1,\n" +
                        "        \"companyName\": \"alibaba3\",\n" +
                        "        \"employeesNumber\": 200,\n" +
                        "        \"employees\": [\n" +
                        "        ]\n" +
                        "    }"))
                .andExpect(jsonPath("$.employeesNumber").value(companyRepository.findById(1).get().getEmployeesNumber()))
                .andExpect(jsonPath("$.companyName").value(companyRepository.findById(1).get().getCompanyName()));
        //then
    }

    @Test
    void should_when_delete_company_by_id_given_company_id() throws Exception {
        //given
        Company company = getMockCompany();
        companyRepository.save(company);
        //when
        mockMvc.perform(delete("/companies/1")).andExpect(status().isOk());
        //then
        assertEquals(false, companyRepository.findById(1).isPresent());
    }

    private List<Company> getMockCompanies() {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1, "xiaoyi", 18, "Male", 30000.0,1));
        employees.add(new Employee(2, "xiaoer", 18, "Male", 30000.0,1));
        employees.add(new Employee(3, "xiaosan", 19, "Male", 30000.0,1));
        employees.add(new Employee(4, "xiaosi", 19, "Male", 30000.0,1));
        employees.add(new Employee(5, "xiaowu", 20, "Male", 30000.0,1));
        employees.add(new Employee(6, "xiaoliu", 20, "Female", 30000.0,1));
        employees.add(new Employee(7, "xiaoqi", 21, "Female", 30000.0,1));
        employees.add(new Employee(8, "xiaoba", 21, "Female", 30000.0,1));
        employees.add(new Employee(9, "xiaojiu", 18, "Male", 30000.0,1));
        employees.add(new Employee(10, "xiaoshi", 18, "Male", 30000.0,1));
        List<Company> companies = new ArrayList<>();
        companies.add(new Company(1, "Mm", 10, employees));
        companies.add(new Company(2, "Aa", 10, employees));
        companies.add(new Company(3, "Bb", 10, employees));
        companies.add(new Company(4, "Cc", 10, employees));
        companies.add(new Company(5, "Dd", 10, employees));
        companies.add(new Company(6, "Ff", 10, employees));
        companies.add(new Company(7, "Gg", 10, employees));
        companies.add(new Company(8, "Ll", 10, employees));
        companies.add(new Company(9, "Pp", 10, employees));
        companies.add(new Company(10, "Uu", 10, employees));
        return companies;
    }

    private Company getMockCompany() {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1, "xiaoyi", 18, "Male", 30000.0,1));
        employees.add(new Employee(2, "xiaoer", 18, "Male", 30000.0,1));
        employees.add(new Employee(3, "xiaosan", 19, "Male", 30000.0,1));
        employees.add(new Employee(4, "xiaosi", 19, "Male", 30000.0,1));
        employees.add(new Employee(5, "xiaowu", 20, "Male", 30000.0,1));
        employees.add(new Employee(6, "xiaoliu", 20, "Female", 30000.0,1));
        employees.add(new Employee(7, "xiaoqi", 21, "Female", 30000.0,1));
        employees.add(new Employee(8, "xiaoba", 21, "Female", 30000.0,1));
        employees.add(new Employee(9, "xiaojiu", 18, "Male", 30000.0,1));
        employees.add(new Employee(10, "xiaoshi", 18, "Male", 30000.0,1));
        return new Company(1, "Mm", 10, employees);
    }

    private List<Employee> getMockEmployees() {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1, "xiaoyi", 18, "Male", 30000.0,1));
        employees.add(new Employee(2, "xiaoer", 18, "Male", 30000.0,1));
        employees.add(new Employee(3, "xiaosan", 19, "Male", 30000.0,1));
        employees.add(new Employee(4, "xiaosi", 19, "Male", 30000.0,1));
        employees.add(new Employee(5, "xiaowu", 20, "Male", 30000.0,1));
        employees.add(new Employee(6, "xiaoliu", 20, "Female", 30000.0,1));
        employees.add(new Employee(7, "xiaoqi", 21, "Female", 30000.0,1));
        employees.add(new Employee(8, "xiaoba", 21, "Female", 30000.0,1));
        employees.add(new Employee(9, "xiaojiu", 18, "Male", 30000.0,1));
        employees.add(new Employee(10, "xiaoshi", 18, "Male", 30000.0,1));
        return employees;
    }

}
