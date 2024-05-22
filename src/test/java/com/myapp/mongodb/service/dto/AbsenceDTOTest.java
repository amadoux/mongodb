package com.myapp.mongodb.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.mongodb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AbsenceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AbsenceDTO.class);
        AbsenceDTO absenceDTO1 = new AbsenceDTO();
        absenceDTO1.setId("id1");
        AbsenceDTO absenceDTO2 = new AbsenceDTO();
        assertThat(absenceDTO1).isNotEqualTo(absenceDTO2);
        absenceDTO2.setId(absenceDTO1.getId());
        assertThat(absenceDTO1).isEqualTo(absenceDTO2);
        absenceDTO2.setId("id2");
        assertThat(absenceDTO1).isNotEqualTo(absenceDTO2);
        absenceDTO1.setId(null);
        assertThat(absenceDTO1).isNotEqualTo(absenceDTO2);
    }
}
