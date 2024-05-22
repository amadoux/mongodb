package com.myapp.mongodb.domain;

import static com.myapp.mongodb.domain.EmployeeTestSamples.*;
import static com.myapp.mongodb.domain.PaySlipTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.mongodb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaySlipTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaySlip.class);
        PaySlip paySlip1 = getPaySlipSample1();
        PaySlip paySlip2 = new PaySlip();
        assertThat(paySlip1).isNotEqualTo(paySlip2);

        paySlip2.setId(paySlip1.getId());
        assertThat(paySlip1).isEqualTo(paySlip2);

        paySlip2 = getPaySlipSample2();
        assertThat(paySlip1).isNotEqualTo(paySlip2);
    }

    @Test
    void employeeTest() throws Exception {
        PaySlip paySlip = getPaySlipRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        paySlip.setEmployee(employeeBack);
        assertThat(paySlip.getEmployee()).isEqualTo(employeeBack);

        paySlip.employee(null);
        assertThat(paySlip.getEmployee()).isNull();
    }
}
