package com.myapp.mongodb.service.mapper;

import com.myapp.mongodb.domain.Employee;
import com.myapp.mongodb.domain.Enterprise;
import com.myapp.mongodb.service.dto.EmployeeDTO;
import com.myapp.mongodb.service.dto.EnterpriseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {
    @Mapping(target = "enterprise", source = "enterprise", qualifiedByName = "enterpriseCompanyName")
    @Mapping(target = "managerEmployee", source = "managerEmployee", qualifiedByName = "employeeEmail")
    EmployeeDTO toDto(Employee s);

    @Named("enterpriseCompanyName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "companyName", source = "companyName")
    EnterpriseDTO toDtoEnterpriseCompanyName(Enterprise enterprise);

    @Named("employeeEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    EmployeeDTO toDtoEmployeeEmail(Employee employee);
}
