package com.myapp.mongodb.domain;

import static com.myapp.mongodb.domain.AbsenceTestSamples.*;
import static com.myapp.mongodb.domain.EmployeeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.mongodb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AbsenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Absence.class);
        Absence absence1 = getAbsenceSample1();
        Absence absence2 = new Absence();
        assertThat(absence1).isNotEqualTo(absence2);

        absence2.setId(absence1.getId());
        assertThat(absence1).isEqualTo(absence2);

        absence2 = getAbsenceSample2();
        assertThat(absence1).isNotEqualTo(absence2);
    }

    @Test
    void employeeTest() throws Exception {
        Absence absence = getAbsenceRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        absence.setEmployee(employeeBack);
        assertThat(absence.getEmployee()).isEqualTo(employeeBack);

        absence.employee(null);
        assertThat(absence.getEmployee()).isNull();
    }
}
