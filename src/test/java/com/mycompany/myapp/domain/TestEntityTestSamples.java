package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TestEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TestEntity getTestEntitySample1() {
        return new TestEntity().id(1L).testPoints("testPoints1").result("result1");
    }

    public static TestEntity getTestEntitySample2() {
        return new TestEntity().id(2L).testPoints("testPoints2").result("result2");
    }

    public static TestEntity getTestEntityRandomSampleGenerator() {
        return new TestEntity()
            .id(longCount.incrementAndGet())
            .testPoints(UUID.randomUUID().toString())
            .result(UUID.randomUUID().toString());
    }
}
