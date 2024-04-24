package xyz.oagueda.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.oagueda.domain.DeviceTestSamples.*;
import static xyz.oagueda.domain.RepairTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.oagueda.web.rest.TestUtil;

class RepairTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Repair.class);
        Repair repair1 = getRepairSample1();
        Repair repair2 = new Repair();
        assertThat(repair1).isNotEqualTo(repair2);

        repair2.setId(repair1.getId());
        assertThat(repair1).isEqualTo(repair2);

        repair2 = getRepairSample2();
        assertThat(repair1).isNotEqualTo(repair2);
    }

    @Test
    void deviceTest() throws Exception {
        Repair repair = getRepairRandomSampleGenerator();
        Device deviceBack = getDeviceRandomSampleGenerator();

        repair.setDevice(deviceBack);
        assertThat(repair.getDevice()).isEqualTo(deviceBack);

        repair.device(null);
        assertThat(repair.getDevice()).isNull();
    }
}
