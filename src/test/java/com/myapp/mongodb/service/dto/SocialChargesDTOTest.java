package com.myapp.mongodb.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.mongodb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SocialChargesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SocialChargesDTO.class);
        SocialChargesDTO socialChargesDTO1 = new SocialChargesDTO();
        socialChargesDTO1.setId("id1");
        SocialChargesDTO socialChargesDTO2 = new SocialChargesDTO();
        assertThat(socialChargesDTO1).isNotEqualTo(socialChargesDTO2);
        socialChargesDTO2.setId(socialChargesDTO1.getId());
        assertThat(socialChargesDTO1).isEqualTo(socialChargesDTO2);
        socialChargesDTO2.setId("id2");
        assertThat(socialChargesDTO1).isNotEqualTo(socialChargesDTO2);
        socialChargesDTO1.setId(null);
        assertThat(socialChargesDTO1).isNotEqualTo(socialChargesDTO2);
    }
}
