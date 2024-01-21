package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MaterialsFileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MaterialsFile getMaterialsFileSample1() {
        return new MaterialsFile().id(1L).materials("materials1");
    }

    public static MaterialsFile getMaterialsFileSample2() {
        return new MaterialsFile().id(2L).materials("materials2");
    }

    public static MaterialsFile getMaterialsFileRandomSampleGenerator() {
        return new MaterialsFile().id(longCount.incrementAndGet()).materials(UUID.randomUUID().toString());
    }
}
