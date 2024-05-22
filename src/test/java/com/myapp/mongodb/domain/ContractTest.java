package com.myapp.mongodb.domain;

import static com.myapp.mongodb.domain.ContractTestSamples.*;
import static com.myapp.mongodb.domain.EmployeeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.mongodb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContractTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contract.class);
        Contract contract1 = getContractSample1();
        Contract contract2 = new Contract();
        assertThat(contract1).isNotEqualTo(contract2);

        contract2.setId(contract1.getId());
        assertThat(contract1).isEqualTo(contract2);

        contract2 = getContractSample2();
        assertThat(contract1).isNotEqualTo(contract2);
    }

    @Test
    void employeeTest() throws Exception {
        Contract contract = getContractRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        contract.setEmployee(employeeBack);
        assertThat(contract.getEmployee()).isEqualTo(employeeBack);

        contract.employee(null);
        assertThat(contract.getEmployee()).isNull();
    }
}
