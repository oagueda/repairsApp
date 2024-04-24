package xyz.oagueda.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PatternTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Pattern getPatternSample1() {
        return new Pattern().id(1L).code("code1");
    }

    public static Pattern getPatternSample2() {
        return new Pattern().id(2L).code("code2");
    }

    public static Pattern getPatternRandomSampleGenerator() {
        return new Pattern().id(longCount.incrementAndGet()).code(UUID.randomUUID().toString());
    }
}
