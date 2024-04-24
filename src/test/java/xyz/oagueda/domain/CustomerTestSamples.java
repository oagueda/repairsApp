package xyz.oagueda.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Customer getCustomerSample1() {
        return new Customer()
            .id(1L)
            .name("name1")
            .nif("nif1")
            .address("address1")
            .city("city1")
            .zipCode("zipCode1")
            .phone1("phone11")
            .phone2("phone21")
            .email("email1");
    }

    public static Customer getCustomerSample2() {
        return new Customer()
            .id(2L)
            .name("name2")
            .nif("nif2")
            .address("address2")
            .city("city2")
            .zipCode("zipCode2")
            .phone1("phone12")
            .phone2("phone22")
            .email("email2");
    }

    public static Customer getCustomerRandomSampleGenerator() {
        return new Customer()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .nif(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .zipCode(UUID.randomUUID().toString())
            .phone1(UUID.randomUUID().toString())
            .phone2(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString());
    }
}
