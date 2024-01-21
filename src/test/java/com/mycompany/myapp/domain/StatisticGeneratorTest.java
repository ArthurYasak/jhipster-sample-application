package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.StatisticGeneratorTestSamples.*;
import static com.mycompany.myapp.domain.TestUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class StatisticGeneratorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StatisticGenerator.class);
        StatisticGenerator statisticGenerator1 = getStatisticGeneratorSample1();
        StatisticGenerator statisticGenerator2 = new StatisticGenerator();
        assertThat(statisticGenerator1).isNotEqualTo(statisticGenerator2);

        statisticGenerator2.setId(statisticGenerator1.getId());
        assertThat(statisticGenerator1).isEqualTo(statisticGenerator2);

        statisticGenerator2 = getStatisticGeneratorSample2();
        assertThat(statisticGenerator1).isNotEqualTo(statisticGenerator2);
    }

    @Test
    void testUserTest() throws Exception {
        StatisticGenerator statisticGenerator = getStatisticGeneratorRandomSampleGenerator();
        TestUser testUserBack = getTestUserRandomSampleGenerator();

        statisticGenerator.addTestUser(testUserBack);
        assertThat(statisticGenerator.getTestUsers()).containsOnly(testUserBack);
        assertThat(testUserBack.getStatisticGenerator()).isEqualTo(statisticGenerator);

        statisticGenerator.removeTestUser(testUserBack);
        assertThat(statisticGenerator.getTestUsers()).doesNotContain(testUserBack);
        assertThat(testUserBack.getStatisticGenerator()).isNull();

        statisticGenerator.testUsers(new HashSet<>(Set.of(testUserBack)));
        assertThat(statisticGenerator.getTestUsers()).containsOnly(testUserBack);
        assertThat(testUserBack.getStatisticGenerator()).isEqualTo(statisticGenerator);

        statisticGenerator.setTestUsers(new HashSet<>());
        assertThat(statisticGenerator.getTestUsers()).doesNotContain(testUserBack);
        assertThat(testUserBack.getStatisticGenerator()).isNull();
    }
}
