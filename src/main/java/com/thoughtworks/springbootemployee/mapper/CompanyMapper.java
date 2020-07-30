package com.thoughtworks.springbootemployee.mapper;

import com.thoughtworks.springbootemployee.dto.CompanyRequest;
import com.thoughtworks.springbootemployee.model.Company;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    public Company toCompany(CompanyRequest companyRequest) {
        Company company = new Company();
        BeanUtils.copyProperties(company, companyRequest);
        return company;
    }
}
