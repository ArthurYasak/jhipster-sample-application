package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ThemeFileCreatorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ThemeFileCreator getThemeFileCreatorSample1() {
        return new ThemeFileCreator().id(1L).filesAmount(1);
    }

    public static ThemeFileCreator getThemeFileCreatorSample2() {
        return new ThemeFileCreator().id(2L).filesAmount(2);
    }

    public static ThemeFileCreator getThemeFileCreatorRandomSampleGenerator() {
        return new ThemeFileCreator().id(longCount.incrementAndGet()).filesAmount(intCount.incrementAndGet());
    }
}
