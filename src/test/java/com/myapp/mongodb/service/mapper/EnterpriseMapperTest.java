package com.myapp.mongodb.service.mapper;

import static com.myapp.mongodb.domain.EnterpriseAsserts.*;
import static com.myapp.mongodb.domain.EnterpriseTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EnterpriseMapperTest {

    private EnterpriseMapper enterpriseMapper;

    @BeforeEach
    void setUp() {
        enterpriseMapper = new EnterpriseMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEnterpriseSample1();
        var actual = enterpriseMapper.toEntity(enterpriseMapper.toDto(expected));
        assertEnterpriseAllPropertiesEquals(expected, actual);
    }
}
