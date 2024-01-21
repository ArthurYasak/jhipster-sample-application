package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MaterialsFileLoaderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MaterialsFileLoader getMaterialsFileLoaderSample1() {
        return new MaterialsFileLoader().id(1L).filesAmount(1);
    }

    public static MaterialsFileLoader getMaterialsFileLoaderSample2() {
        return new MaterialsFileLoader().id(2L).filesAmount(2);
    }

    public static MaterialsFileLoader getMaterialsFileLoaderRandomSampleGenerator() {
        return new MaterialsFileLoader().id(longCount.incrementAndGet()).filesAmount(intCount.incrementAndGet());
    }
}
