package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class TestUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TestUser getTestUserSample1() {
        return new TestUser().id(1L);
    }

    public static TestUser getTestUserSample2() {
        return new TestUser().id(2L);
    }

    public static TestUser getTestUserRandomSampleGenerator() {
        return new TestUser().id(longCount.incrementAndGet());
    }
}
