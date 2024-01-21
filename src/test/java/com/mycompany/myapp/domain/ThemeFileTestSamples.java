package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ThemeFileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ThemeFile getThemeFileSample1() {
        return new ThemeFile().id(1L).theme("theme1");
    }

    public static ThemeFile getThemeFileSample2() {
        return new ThemeFile().id(2L).theme("theme2");
    }

    public static ThemeFile getThemeFileRandomSampleGenerator() {
        return new ThemeFile().id(longCount.incrementAndGet()).theme(UUID.randomUUID().toString());
    }
}
