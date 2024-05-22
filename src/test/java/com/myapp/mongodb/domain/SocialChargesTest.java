package com.myapp.mongodb.domain;

import static com.myapp.mongodb.domain.EmployeeTestSamples.*;
import static com.myapp.mongodb.domain.EnterpriseTestSamples.*;
import static com.myapp.mongodb.domain.SocialChargesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.mongodb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SocialChargesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SocialCharges.class);
        SocialCharges socialCharges1 = getSocialChargesSample1();
        SocialCharges socialCharges2 = new SocialCharges();
        assertThat(socialCharges1).isNotEqualTo(socialCharges2);

        socialCharges2.setId(socialCharges1.getId());
        assertThat(socialCharges1).isEqualTo(socialCharges2);

        socialCharges2 = getSocialChargesSample2();
        assertThat(socialCharges1).isNotEqualTo(socialCharges2);
    }

    @Test
    void responsableDepenseTest() throws Exception {
        SocialCharges socialCharges = getSocialChargesRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        socialCharges.setResponsableDepense(employeeBack);
        assertThat(socialCharges.getResponsableDepense()).isEqualTo(employeeBack);

        socialCharges.responsableDepense(null);
        assertThat(socialCharges.getResponsableDepense()).isNull();
    }

    @Test
    void enterpriseTest() throws Exception {
        SocialCharges socialCharges = getSocialChargesRandomSampleGenerator();
        Enterprise enterpriseBack = getEnterpriseRandomSampleGenerator();

        socialCharges.setEnterprise(enterpriseBack);
        assertThat(socialCharges.getEnterprise()).isEqualTo(enterpriseBack);

        socialCharges.enterprise(null);
        assertThat(socialCharges.getEnterprise()).isNull();
    }
}
