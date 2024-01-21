package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReportSenderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ReportSender getReportSenderSample1() {
        return new ReportSender().id(1L).emailList("emailList1");
    }

    public static ReportSender getReportSenderSample2() {
        return new ReportSender().id(2L).emailList("emailList2");
    }

    public static ReportSender getReportSenderRandomSampleGenerator() {
        return new ReportSender().id(longCount.incrementAndGet()).emailList(UUID.randomUUID().toString());
    }
}
