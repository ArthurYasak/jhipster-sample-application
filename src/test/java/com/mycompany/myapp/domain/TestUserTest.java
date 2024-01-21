package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ReportSenderTestSamples.*;
import static com.mycompany.myapp.domain.StatisticGeneratorTestSamples.*;
import static com.mycompany.myapp.domain.TestEntityTestSamples.*;
import static com.mycompany.myapp.domain.TestUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TestUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestUser.class);
        TestUser testUser1 = getTestUserSample1();
        TestUser testUser2 = new TestUser();
        assertThat(testUser1).isNotEqualTo(testUser2);

        testUser2.setId(testUser1.getId());
        assertThat(testUser1).isEqualTo(testUser2);

        testUser2 = getTestUserSample2();
        assertThat(testUser1).isNotEqualTo(testUser2);
    }

    @Test
    void reportSenderTest() throws Exception {
        TestUser testUser = getTestUserRandomSampleGenerator();
        ReportSender reportSenderBack = getReportSenderRandomSampleGenerator();

        testUser.setReportSender(reportSenderBack);
        assertThat(testUser.getReportSender()).isEqualTo(reportSenderBack);

        testUser.reportSender(null);
        assertThat(testUser.getReportSender()).isNull();
    }

    @Test
    void statisticGeneratorTest() throws Exception {
        TestUser testUser = getTestUserRandomSampleGenerator();
        StatisticGenerator statisticGeneratorBack = getStatisticGeneratorRandomSampleGenerator();

        testUser.setStatisticGenerator(statisticGeneratorBack);
        assertThat(testUser.getStatisticGenerator()).isEqualTo(statisticGeneratorBack);

        testUser.statisticGenerator(null);
        assertThat(testUser.getStatisticGenerator()).isNull();
    }

    @Test
    void testEntityTest() throws Exception {
        TestUser testUser = getTestUserRandomSampleGenerator();
        TestEntity testEntityBack = getTestEntityRandomSampleGenerator();

        testUser.addTestEntity(testEntityBack);
        assertThat(testUser.getTestEntities()).containsOnly(testEntityBack);
        assertThat(testEntityBack.getTestUsers()).containsOnly(testUser);

        testUser.removeTestEntity(testEntityBack);
        assertThat(testUser.getTestEntities()).doesNotContain(testEntityBack);
        assertThat(testEntityBack.getTestUsers()).doesNotContain(testUser);

        testUser.testEntities(new HashSet<>(Set.of(testEntityBack)));
        assertThat(testUser.getTestEntities()).containsOnly(testEntityBack);
        assertThat(testEntityBack.getTestUsers()).containsOnly(testUser);

        testUser.setTestEntities(new HashSet<>());
        assertThat(testUser.getTestEntities()).doesNotContain(testEntityBack);
        assertThat(testEntityBack.getTestUsers()).doesNotContain(testUser);
    }
}
