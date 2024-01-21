package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TestCreatorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TestCreator getTestCreatorSample1() {
        return new TestCreator().id(1L).testsAmount(1);
    }

    public static TestCreator getTestCreatorSample2() {
        return new TestCreator().id(2L).testsAmount(2);
    }

    public static TestCreator getTestCreatorRandomSampleGenerator() {
        return new TestCreator().id(longCount.incrementAndGet()).testsAmount(intCount.incrementAndGet());
    }
}
