package com.myapp.mongodb.service.mapper;

import com.myapp.mongodb.domain.Enterprise;
import com.myapp.mongodb.service.dto.EnterpriseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Enterprise} and its DTO {@link EnterpriseDTO}.
 */
@Mapper(componentModel = "spring")
public interface EnterpriseMapper extends EntityMapper<EnterpriseDTO, Enterprise> {}
