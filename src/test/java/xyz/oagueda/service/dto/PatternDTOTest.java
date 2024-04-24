package xyz.oagueda.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.oagueda.web.rest.TestUtil;

class PatternDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PatternDTO.class);
        PatternDTO patternDTO1 = new PatternDTO();
        patternDTO1.setId(1L);
        PatternDTO patternDTO2 = new PatternDTO();
        assertThat(patternDTO1).isNotEqualTo(patternDTO2);
        patternDTO2.setId(patternDTO1.getId());
        assertThat(patternDTO1).isEqualTo(patternDTO2);
        patternDTO2.setId(2L);
        assertThat(patternDTO1).isNotEqualTo(patternDTO2);
        patternDTO1.setId(null);
        assertThat(patternDTO1).isNotEqualTo(patternDTO2);
    }
}
