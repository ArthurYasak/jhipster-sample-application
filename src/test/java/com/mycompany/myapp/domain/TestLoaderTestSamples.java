package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TestLoaderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TestLoader getTestLoaderSample1() {
        return new TestLoader().id(1L).testsAmount(1);
    }

    public static TestLoader getTestLoaderSample2() {
        return new TestLoader().id(2L).testsAmount(2);
    }

    public static TestLoader getTestLoaderRandomSampleGenerator() {
        return new TestLoader().id(longCount.incrementAndGet()).testsAmount(intCount.incrementAndGet());
    }
}
