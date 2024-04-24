package xyz.oagueda.service.mapper;

import static xyz.oagueda.domain.DeviceAsserts.*;
import static xyz.oagueda.domain.DeviceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeviceMapperTest {

    private DeviceMapper deviceMapper;

    @BeforeEach
    void setUp() {
        deviceMapper = new DeviceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDeviceSample1();
        var actual = deviceMapper.toEntity(deviceMapper.toDto(expected));
        assertDeviceAllPropertiesEquals(expected, actual);
    }
}
