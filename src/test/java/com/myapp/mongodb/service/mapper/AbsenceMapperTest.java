package com.myapp.mongodb.service.mapper;

import static com.myapp.mongodb.domain.AbsenceAsserts.*;
import static com.myapp.mongodb.domain.AbsenceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AbsenceMapperTest {

    private AbsenceMapper absenceMapper;

    @BeforeEach
    void setUp() {
        absenceMapper = new AbsenceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAbsenceSample1();
        var actual = absenceMapper.toEntity(absenceMapper.toDto(expected));
        assertAbsenceAllPropertiesEquals(expected, actual);
    }
}
