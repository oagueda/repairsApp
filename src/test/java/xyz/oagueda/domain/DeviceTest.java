package xyz.oagueda.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.oagueda.domain.CustomerTestSamples.*;
import static xyz.oagueda.domain.DeviceTestSamples.*;
import static xyz.oagueda.domain.PatternTestSamples.*;
import static xyz.oagueda.domain.RepairTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.oagueda.web.rest.TestUtil;

class DeviceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Device.class);
        Device device1 = getDeviceSample1();
        Device device2 = new Device();
        assertThat(device1).isNotEqualTo(device2);

        device2.setId(device1.getId());
        assertThat(device1).isEqualTo(device2);

        device2 = getDeviceSample2();
        assertThat(device1).isNotEqualTo(device2);
    }

    @Test
    void patternTest() throws Exception {
        Device device = getDeviceRandomSampleGenerator();
        Pattern patternBack = getPatternRandomSampleGenerator();

        device.setPattern(patternBack);
        assertThat(device.getPattern()).isEqualTo(patternBack);

        device.pattern(null);
        assertThat(device.getPattern()).isNull();
    }

    @Test
    void repairTest() throws Exception {
        Device device = getDeviceRandomSampleGenerator();
        Repair repairBack = getRepairRandomSampleGenerator();

        device.addRepair(repairBack);
        assertThat(device.getRepairs()).containsOnly(repairBack);
        assertThat(repairBack.getDevice()).isEqualTo(device);

        device.removeRepair(repairBack);
        assertThat(device.getRepairs()).doesNotContain(repairBack);
        assertThat(repairBack.getDevice()).isNull();

        device.repairs(new HashSet<>(Set.of(repairBack)));
        assertThat(device.getRepairs()).containsOnly(repairBack);
        assertThat(repairBack.getDevice()).isEqualTo(device);

        device.setRepairs(new HashSet<>());
        assertThat(device.getRepairs()).doesNotContain(repairBack);
        assertThat(repairBack.getDevice()).isNull();
    }

    @Test
    void customerTest() throws Exception {
        Device device = getDeviceRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        device.setCustomer(customerBack);
        assertThat(device.getCustomer()).isEqualTo(customerBack);

        device.customer(null);
        assertThat(device.getCustomer()).isNull();
    }
}
