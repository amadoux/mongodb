package com.myapp.mongodb.service.mapper;

import com.myapp.mongodb.domain.Absence;
import com.myapp.mongodb.domain.Employee;
import com.myapp.mongodb.service.dto.AbsenceDTO;
import com.myapp.mongodb.service.dto.EmployeeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Absence} and its DTO {@link AbsenceDTO}.
 */
@Mapper(componentModel = "spring")
public interface AbsenceMapper extends EntityMapper<AbsenceDTO, Absence> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeEmail")
    AbsenceDTO toDto(Absence s);

    @Named("employeeEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    EmployeeDTO toDtoEmployeeEmail(Employee employee);
}
