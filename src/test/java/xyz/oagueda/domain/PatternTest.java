package xyz.oagueda.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.oagueda.domain.DeviceTestSamples.*;
import static xyz.oagueda.domain.PatternTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.oagueda.web.rest.TestUtil;

class PatternTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pattern.class);
        Pattern pattern1 = getPatternSample1();
        Pattern pattern2 = new Pattern();
        assertThat(pattern1).isNotEqualTo(pattern2);

        pattern2.setId(pattern1.getId());
        assertThat(pattern1).isEqualTo(pattern2);

        pattern2 = getPatternSample2();
        assertThat(pattern1).isNotEqualTo(pattern2);
    }

    @Test
    void deviceTest() throws Exception {
        Pattern pattern = getPatternRandomSampleGenerator();
        Device deviceBack = getDeviceRandomSampleGenerator();

        pattern.setDevice(deviceBack);
        assertThat(pattern.getDevice()).isEqualTo(deviceBack);
        assertThat(deviceBack.getPattern()).isEqualTo(pattern);

        pattern.device(null);
        assertThat(pattern.getDevice()).isNull();
        assertThat(deviceBack.getPattern()).isNull();
    }
}
