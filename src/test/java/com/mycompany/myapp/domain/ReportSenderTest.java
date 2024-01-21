package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ReportSenderTestSamples.*;
import static com.mycompany.myapp.domain.TestUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ReportSenderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportSender.class);
        ReportSender reportSender1 = getReportSenderSample1();
        ReportSender reportSender2 = new ReportSender();
        assertThat(reportSender1).isNotEqualTo(reportSender2);

        reportSender2.setId(reportSender1.getId());
        assertThat(reportSender1).isEqualTo(reportSender2);

        reportSender2 = getReportSenderSample2();
        assertThat(reportSender1).isNotEqualTo(reportSender2);
    }

    @Test
    void testUserTest() throws Exception {
        ReportSender reportSender = getReportSenderRandomSampleGenerator();
        TestUser testUserBack = getTestUserRandomSampleGenerator();

        reportSender.addTestUser(testUserBack);
        assertThat(reportSender.getTestUsers()).containsOnly(testUserBack);
        assertThat(testUserBack.getReportSender()).isEqualTo(reportSender);

        reportSender.removeTestUser(testUserBack);
        assertThat(reportSender.getTestUsers()).doesNotContain(testUserBack);
        assertThat(testUserBack.getReportSender()).isNull();

        reportSender.testUsers(new HashSet<>(Set.of(testUserBack)));
        assertThat(reportSender.getTestUsers()).containsOnly(testUserBack);
        assertThat(testUserBack.getReportSender()).isEqualTo(reportSender);

        reportSender.setTestUsers(new HashSet<>());
        assertThat(reportSender.getTestUsers()).doesNotContain(testUserBack);
        assertThat(testUserBack.getReportSender()).isNull();
    }
}
