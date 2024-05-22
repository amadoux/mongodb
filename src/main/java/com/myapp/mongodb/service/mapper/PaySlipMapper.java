package com.myapp.mongodb.service.mapper;

import com.myapp.mongodb.domain.Employee;
import com.myapp.mongodb.domain.PaySlip;
import com.myapp.mongodb.service.dto.EmployeeDTO;
import com.myapp.mongodb.service.dto.PaySlipDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaySlip} and its DTO {@link PaySlipDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaySlipMapper extends EntityMapper<PaySlipDTO, PaySlip> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeEmail")
    PaySlipDTO toDto(PaySlip s);

    @Named("employeeEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    EmployeeDTO toDtoEmployeeEmail(Employee employee);
}
