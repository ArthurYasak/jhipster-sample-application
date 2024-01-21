package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TestEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TestEntity getTestEntitySample1() {
        return new TestEntity().id(1L).question("question1").testPoints("testPoints1").result(1);
    }

    public static TestEntity getTestEntitySample2() {
        return new TestEntity().id(2L).question("question2").testPoints("testPoints2").result(2);
    }

    public static TestEntity getTestEntityRandomSampleGenerator() {
        return new TestEntity()
            .id(longCount.incrementAndGet())
            .question(UUID.randomUUID().toString())
            .testPoints(UUID.randomUUID().toString())
            .result(intCount.incrementAndGet());
    }
}
