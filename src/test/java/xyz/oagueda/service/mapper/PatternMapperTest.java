package xyz.oagueda.service.mapper;

import static xyz.oagueda.domain.PatternAsserts.*;
import static xyz.oagueda.domain.PatternTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PatternMapperTest {

    private PatternMapper patternMapper;

    @BeforeEach
    void setUp() {
        patternMapper = new PatternMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPatternSample1();
        var actual = patternMapper.toEntity(patternMapper.toDto(expected));
        assertPatternAllPropertiesEquals(expected, actual);
    }
}
