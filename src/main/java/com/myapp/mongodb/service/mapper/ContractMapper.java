package com.myapp.mongodb.service.mapper;

import com.myapp.mongodb.domain.Contract;
import com.myapp.mongodb.domain.Employee;
import com.myapp.mongodb.service.dto.ContractDTO;
import com.myapp.mongodb.service.dto.EmployeeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contract} and its DTO {@link ContractDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContractMapper extends EntityMapper<ContractDTO, Contract> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeEmail")
    ContractDTO toDto(Contract s);

    @Named("employeeEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    EmployeeDTO toDtoEmployeeEmail(Employee employee);
}
