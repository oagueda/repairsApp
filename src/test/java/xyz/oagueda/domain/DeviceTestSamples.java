package xyz.oagueda.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DeviceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Device getDeviceSample1() {
        return new Device()
            .id(1L)
            .brand("brand1")
            .model("model1")
            .serialNumber("serialNumber1")
            .password("password1")
            .simPinCode("simPinCode1");
    }

    public static Device getDeviceSample2() {
        return new Device()
            .id(2L)
            .brand("brand2")
            .model("model2")
            .serialNumber("serialNumber2")
            .password("password2")
            .simPinCode("simPinCode2");
    }

    public static Device getDeviceRandomSampleGenerator() {
        return new Device()
            .id(longCount.incrementAndGet())
            .brand(UUID.randomUUID().toString())
            .model(UUID.randomUUID().toString())
            .serialNumber(UUID.randomUUID().toString())
            .password(UUID.randomUUID().toString())
            .simPinCode(UUID.randomUUID().toString());
    }
}
