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

import static org.hamcrest.Matchers.hasSize;
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
                .andExpect(jsonPath("$[0].companyId").isNumber())
                .andExpect(jsonPath("$[0].companyId").value(companies.get(0).getId()));
        //then
    }

    @Test
    void should_return_employees_when_getAllEmployees_given_() throws Exception {
        //given
        employeeRepository.saveAll(getMockEmployees());
        List<Company> companies = companyRepository.saveAll(getMockCompanies());
        Company company = companyRepository.findById(1).orElse(null);
        List<Employee> employeesRepo = employeeRepository.findAll();
        List<Company> companieRepo = companyRepository.findAll();
        List<Employee> employees = company.getEmployees();
        //when
        mockMvc.perform(get("/companies/1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(10)));

        //then

    }

    @Test
    void should_return_company_when_get_company_by_id_given_company_id() throws Exception {
        List<Company> companies = companyRepository.saveAll(getMockCompanies());
        //given
        mockMvc.perform(get("/companies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyId").value(companies.get(0).getId()))
                .andExpect(jsonPath("$.employeesNumber").value(companies.get(0).getEmployeesNumber()))
                .andExpect(jsonPath("$.companyName").value(companies.get(0).getCompanyName()));
        //when

        //then

    }

    @Test
    void should_return_company_when_add_company_given_company() throws Exception {
        //given
        Company company = new Company(1, "alibaba2", 100 , Collections.emptyList());
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
        Company company = new Company(1, "alibaba2", 100 , Collections.emptyList());
        companyRepository.save(company);
        //when
        mockMvc.perform(put("/companies/1").contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "        \"id\": 1,\n" +
                        "        \"companyName\": \"alibaba2\",\n" +
                        "        \"employeesNumber\": 200,\n" +
                        "        \"employees\": [\n" +
                        "        ]\n" +
                        "    }"))
                .andExpect(jsonPath("$.employeesNumber").value(companyRepository.findById(1).get().getEmployeesNumber()));
        //then
    }





    private List<Company> getMockCompanies() {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1, "xiaoyi", 18, "Male", 30000.0));
        employees.add(new Employee(2, "xiaoer", 18, "Male", 30000.0));
        employees.add(new Employee(3, "xiaosan", 19, "Male", 30000.0));
        employees.add(new Employee(4, "xiaosi", 19, "Male", 30000.0));
        employees.add(new Employee(5, "xiaowu", 20, "Male", 30000.0));
        employees.add(new Employee(6, "xiaoliu", 20, "Female",30000.0));
        employees.add(new Employee(7, "xiaoqi", 21, "Female", 30000.0));
        employees.add(new Employee(8, "xiaoba", 21, "Female", 30000.0));
        employees.add(new Employee(9, "xiaojiu", 18, "Male", 30000.0));
        employees.add(new Employee(10, "xiaoshi", 18, "Male", 30000.0));
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
        employees.add(new Employee(1, "xiaoyi", 18, "Male",30000.0));
        employees.add(new Employee(2, "xiaoer", 18, "Male", 30000.0));
        employees.add(new Employee(3, "xiaosan", 19, "Male", 30000.0));
        employees.add(new Employee(4, "xiaosi", 19, "Male", 30000.0));
        employees.add(new Employee(5, "xiaowu", 20, "Male", 30000.0));
        employees.add(new Employee(6, "xiaoliu", 20, "Female", 30000.0));
        employees.add(new Employee(7, "xiaoqi", 21, "Female", 30000.0));
        employees.add(new Employee(8, "xiaoba", 21, "Female", 30000.0));
        employees.add(new Employee(9, "xiaojiu", 18, "Male", 30000.0));
        employees.add(new Employee(10, "xiaoshi", 18, "Male", 30000.0));
        return new Company(1, "Mm", 10, employees);
    }

    private List<Employee> getMockEmployees(){
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1, "xiaoyi", 18, "Male", 30000.0));
        employees.add(new Employee(2, "xiaoer", 18, "Male", 30000.0));
        employees.add(new Employee(3, "xiaosan", 19, "Male", 30000.0));
        employees.add(new Employee(4, "xiaosi", 19, "Male", 30000.0));
        employees.add(new Employee(5, "xiaowu", 20, "Male", 30000.0));
        employees.add(new Employee(6, "xiaoliu", 20, "Female",30000.0));
        employees.add(new Employee(7, "xiaoqi", 21, "Female", 30000.0));
        employees.add(new Employee(8, "xiaoba", 21, "Female", 30000.0));
        employees.add(new Employee(9, "xiaojiu", 18, "Male", 30000.0));
        employees.add(new Employee(10, "xiaoshi", 18, "Male", 30000.0));
        return employees;
    }

}
