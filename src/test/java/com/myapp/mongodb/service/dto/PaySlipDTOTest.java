package com.myapp.mongodb.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.mongodb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaySlipDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaySlipDTO.class);
        PaySlipDTO paySlipDTO1 = new PaySlipDTO();
        paySlipDTO1.setId("id1");
        PaySlipDTO paySlipDTO2 = new PaySlipDTO();
        assertThat(paySlipDTO1).isNotEqualTo(paySlipDTO2);
        paySlipDTO2.setId(paySlipDTO1.getId());
        assertThat(paySlipDTO1).isEqualTo(paySlipDTO2);
        paySlipDTO2.setId("id2");
        assertThat(paySlipDTO1).isNotEqualTo(paySlipDTO2);
        paySlipDTO1.setId(null);
        assertThat(paySlipDTO1).isNotEqualTo(paySlipDTO2);
    }
}
