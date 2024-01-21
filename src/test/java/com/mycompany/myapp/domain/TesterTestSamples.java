package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TesterTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Tester getTesterSample1() {
        return new Tester().id(1L).holdTests(1);
    }

    public static Tester getTesterSample2() {
        return new Tester().id(2L).holdTests(2);
    }

    public static Tester getTesterRandomSampleGenerator() {
        return new Tester().id(longCount.incrementAndGet()).holdTests(intCount.incrementAndGet());
    }
}
