package xyz.oagueda.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.oagueda.domain.CustomerTestSamples.*;
import static xyz.oagueda.domain.DeviceTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.oagueda.web.rest.TestUtil;

class CustomerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customer.class);
        Customer customer1 = getCustomerSample1();
        Customer customer2 = new Customer();
        assertThat(customer1).isNotEqualTo(customer2);

        customer2.setId(customer1.getId());
        assertThat(customer1).isEqualTo(customer2);

        customer2 = getCustomerSample2();
        assertThat(customer1).isNotEqualTo(customer2);
    }

    @Test
    void deviceTest() throws Exception {
        Customer customer = getCustomerRandomSampleGenerator();
        Device deviceBack = getDeviceRandomSampleGenerator();

        customer.addDevice(deviceBack);
        assertThat(customer.getDevices()).containsOnly(deviceBack);
        assertThat(deviceBack.getCustomer()).isEqualTo(customer);

        customer.removeDevice(deviceBack);
        assertThat(customer.getDevices()).doesNotContain(deviceBack);
        assertThat(deviceBack.getCustomer()).isNull();

        customer.devices(new HashSet<>(Set.of(deviceBack)));
        assertThat(customer.getDevices()).containsOnly(deviceBack);
        assertThat(deviceBack.getCustomer()).isEqualTo(customer);

        customer.setDevices(new HashSet<>());
        assertThat(customer.getDevices()).doesNotContain(deviceBack);
        assertThat(deviceBack.getCustomer()).isNull();
    }
}
