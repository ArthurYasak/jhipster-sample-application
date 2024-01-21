package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class StatisticGeneratorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static StatisticGenerator getStatisticGeneratorSample1() {
        return new StatisticGenerator().id(1L).generatedReportsAmount(1);
    }

    public static StatisticGenerator getStatisticGeneratorSample2() {
        return new StatisticGenerator().id(2L).generatedReportsAmount(2);
    }

    public static StatisticGenerator getStatisticGeneratorRandomSampleGenerator() {
        return new StatisticGenerator().id(longCount.incrementAndGet()).generatedReportsAmount(intCount.incrementAndGet());
    }
}
