package xyz.oagueda.service.mapper;

import static xyz.oagueda.domain.RepairAsserts.*;
import static xyz.oagueda.domain.RepairTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RepairMapperTest {

    private RepairMapper repairMapper;

    @BeforeEach
    void setUp() {
        repairMapper = new RepairMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRepairSample1();
        var actual = repairMapper.toEntity(repairMapper.toDto(expected));
        assertRepairAllPropertiesEquals(expected, actual);
    }
}
