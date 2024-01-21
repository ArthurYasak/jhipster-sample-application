package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.TestEntityTestSamples.*;
import static com.mycompany.myapp.domain.TesterTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TesterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tester.class);
        Tester tester1 = getTesterSample1();
        Tester tester2 = new Tester();
        assertThat(tester1).isNotEqualTo(tester2);

        tester2.setId(tester1.getId());
        assertThat(tester1).isEqualTo(tester2);

        tester2 = getTesterSample2();
        assertThat(tester1).isNotEqualTo(tester2);
    }

    @Test
    void testEntityTest() throws Exception {
        Tester tester = getTesterRandomSampleGenerator();
        TestEntity testEntityBack = getTestEntityRandomSampleGenerator();

        tester.addTestEntity(testEntityBack);
        assertThat(tester.getTestEntities()).containsOnly(testEntityBack);
        assertThat(testEntityBack.getTester()).isEqualTo(tester);

        tester.removeTestEntity(testEntityBack);
        assertThat(tester.getTestEntities()).doesNotContain(testEntityBack);
        assertThat(testEntityBack.getTester()).isNull();

        tester.testEntities(new HashSet<>(Set.of(testEntityBack)));
        assertThat(tester.getTestEntities()).containsOnly(testEntityBack);
        assertThat(testEntityBack.getTester()).isEqualTo(tester);

        tester.setTestEntities(new HashSet<>());
        assertThat(tester.getTestEntities()).doesNotContain(testEntityBack);
        assertThat(testEntityBack.getTester()).isNull();
    }
}
