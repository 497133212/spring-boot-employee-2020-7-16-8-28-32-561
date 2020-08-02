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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeIntegrationTest {

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
    void should_return_employee_list_when_getAllEmployees_given_request() throws Exception {
        //given
        List<Employee> employees = employeeRepository.saveAll(getMockEmployees());
        //when
        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(10))
                .andExpect(jsonPath("$.msg").value("succeed"))
                .andExpect(jsonPath("$.data[0].id").value(employees.get(0).getId()));
        //then
    }

    @Test
    void should_return_empanees_when_get_employees_by_id_given_employee_id() throws Exception {
        //given
        List<Employee> employees = employeeRepository.saveAll(getMockEmployees());
        //when
        mockMvc.perform(get("/employees/" + employees.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(employees.get(0).getId()))
                .andExpect(jsonPath("$.data.name").value(employees.get(0).getName()))
                .andExpect(jsonPath("$.data.age").value(employees.get(0).getAge()))
                .andExpect(jsonPath("$.data.gender").value(employees.get(0).getGender()));
        //then
    }

    @Test
    void should_return_employees_list_when_get_all_employees_after_Pagination_given_page_and_pageSize() throws Exception {
        //given
        List<Employee> employees = employeeRepository.saveAll(getMockEmployees());
        //when
        mockMvc.perform(get("/employees")
                .param("page", "1")
                .param("pageSize", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.numberOfElements").value(5));
    }

    @Test
    void should_return_employee_when_add_employee_given_employee_request() throws Exception {
        //given
        Employee employee = employeeRepository.save(new Employee(1, "Lin", 18, "male", 3000.0, null));
        String json = JSON.toJSONString(employee);
        //when
        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(employee.getId()))
                .andExpect(jsonPath("$.data.name").value(employee.getName()))
                .andExpect(jsonPath("$.data.age").value(employee.getAge()))
                .andExpect(jsonPath("$.data.gender").value(employee.getGender()));
        //then
    }

    @Test
    void should_return_employee_when_update_employee_given_employee_request() throws Exception {
        //given
        Employee employee = employeeRepository.save(new Employee(1, "Lin", 18, "male", 3000.0, null));
        Employee updateEmployee = new Employee(employee.getId(), "Ming", 20, "female",4000.0, null);
        String json = JSON.toJSONString(updateEmployee);
        //when
        mockMvc.perform(put("/employees/" + employee.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.data.id").value(updateEmployee.getId()))
                .andExpect(jsonPath("$.data.name").value(updateEmployee.getName()))
                .andExpect(jsonPath("$.data.age").value(updateEmployee.getAge()))
                .andExpect(jsonPath("$.data.gender").value(updateEmployee.getGender()));
        //then
    }

    private List<Employee> getMockEmployees() {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1, "xiaoyi", 18, "Male", 30000.0, null));
        employees.add(new Employee(2, "xiaoer", 18, "Male", 30000.0, null));
        employees.add(new Employee(3, "xiaosan", 19, "Male", 30000.0, null));
        employees.add(new Employee(4, "xiaosi", 19, "Male", 30000.0, null));
        employees.add(new Employee(5, "xiaowu", 20, "Male", 30000.0, null));
        employees.add(new Employee(6, "xiaoliu", 20, "Female", 30000.0, null));
        employees.add(new Employee(7, "xiaoqi", 21, "Female", 30000.0, null));
        employees.add(new Employee(8, "xiaoba", 21, "Female", 30000.0, null));
        employees.add(new Employee(9, "xiaojiu", 18, "Male", 30000.0, null));
        employees.add(new Employee(10, "xiaoshi", 18, "Male", 30000.0, null));
        return employees;
    }
}
