package com.myapp.mongodb.domain;

import static com.myapp.mongodb.domain.EmployeeTestSamples.*;
import static com.myapp.mongodb.domain.EmployeeTestSamples.*;
import static com.myapp.mongodb.domain.EnterpriseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.mongodb.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EmployeeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employee.class);
        Employee employee1 = getEmployeeSample1();
        Employee employee2 = new Employee();
        assertThat(employee1).isNotEqualTo(employee2);

        employee2.setId(employee1.getId());
        assertThat(employee1).isEqualTo(employee2);

        employee2 = getEmployeeSample2();
        assertThat(employee1).isNotEqualTo(employee2);
    }

    @Test
    void enterpriseTest() throws Exception {
        Employee employee = getEmployeeRandomSampleGenerator();
        Enterprise enterpriseBack = getEnterpriseRandomSampleGenerator();

        employee.setEnterprise(enterpriseBack);
        assertThat(employee.getEnterprise()).isEqualTo(enterpriseBack);

        employee.enterprise(null);
        assertThat(employee.getEnterprise()).isNull();
    }

    @Test
    void managerEmployeeTest() throws Exception {
        Employee employee = getEmployeeRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        employee.setManagerEmployee(employeeBack);
        assertThat(employee.getManagerEmployee()).isEqualTo(employeeBack);

        employee.managerEmployee(null);
        assertThat(employee.getManagerEmployee()).isNull();
    }

    @Test
    void managerTest() throws Exception {
        Employee employee = getEmployeeRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        employee.addManager(employeeBack);
        assertThat(employee.getManagers()).containsOnly(employeeBack);
        assertThat(employeeBack.getManagerEmployee()).isEqualTo(employee);

        employee.removeManager(employeeBack);
        assertThat(employee.getManagers()).doesNotContain(employeeBack);
        assertThat(employeeBack.getManagerEmployee()).isNull();

        employee.managers(new HashSet<>(Set.of(employeeBack)));
        assertThat(employee.getManagers()).containsOnly(employeeBack);
        assertThat(employeeBack.getManagerEmployee()).isEqualTo(employee);

        employee.setManagers(new HashSet<>());
        assertThat(employee.getManagers()).doesNotContain(employeeBack);
        assertThat(employeeBack.getManagerEmployee()).isNull();
    }
}
