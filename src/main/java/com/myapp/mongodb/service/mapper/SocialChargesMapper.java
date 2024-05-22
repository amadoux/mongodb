package com.myapp.mongodb.service.mapper;

import com.myapp.mongodb.domain.Employee;
import com.myapp.mongodb.domain.Enterprise;
import com.myapp.mongodb.domain.SocialCharges;
import com.myapp.mongodb.service.dto.EmployeeDTO;
import com.myapp.mongodb.service.dto.EnterpriseDTO;
import com.myapp.mongodb.service.dto.SocialChargesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SocialCharges} and its DTO {@link SocialChargesDTO}.
 */
@Mapper(componentModel = "spring")
public interface SocialChargesMapper extends EntityMapper<SocialChargesDTO, SocialCharges> {
    @Mapping(target = "responsableDepense", source = "responsableDepense", qualifiedByName = "employeeEmail")
    @Mapping(target = "enterprise", source = "enterprise", qualifiedByName = "enterpriseCompanyName")
    SocialChargesDTO toDto(SocialCharges s);

    @Named("employeeEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    EmployeeDTO toDtoEmployeeEmail(Employee employee);

    @Named("enterpriseCompanyName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "companyName", source = "companyName")
    EnterpriseDTO toDtoEnterpriseCompanyName(Enterprise enterprise);
}
