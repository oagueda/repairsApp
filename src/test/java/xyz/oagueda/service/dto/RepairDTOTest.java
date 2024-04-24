package xyz.oagueda.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.oagueda.web.rest.TestUtil;

class RepairDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RepairDTO.class);
        RepairDTO repairDTO1 = new RepairDTO();
        repairDTO1.setId(1L);
        RepairDTO repairDTO2 = new RepairDTO();
        assertThat(repairDTO1).isNotEqualTo(repairDTO2);
        repairDTO2.setId(repairDTO1.getId());
        assertThat(repairDTO1).isEqualTo(repairDTO2);
        repairDTO2.setId(2L);
        assertThat(repairDTO1).isNotEqualTo(repairDTO2);
        repairDTO1.setId(null);
        assertThat(repairDTO1).isNotEqualTo(repairDTO2);
    }
}
