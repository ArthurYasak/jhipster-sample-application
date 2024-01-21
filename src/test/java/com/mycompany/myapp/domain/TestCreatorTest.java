package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.TestCreatorTestSamples.*;
import static com.mycompany.myapp.domain.TestEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TestCreatorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestCreator.class);
        TestCreator testCreator1 = getTestCreatorSample1();
        TestCreator testCreator2 = new TestCreator();
        assertThat(testCreator1).isNotEqualTo(testCreator2);

        testCreator2.setId(testCreator1.getId());
        assertThat(testCreator1).isEqualTo(testCreator2);

        testCreator2 = getTestCreatorSample2();
        assertThat(testCreator1).isNotEqualTo(testCreator2);
    }

    @Test
    void testEntityTest() throws Exception {
        TestCreator testCreator = getTestCreatorRandomSampleGenerator();
        TestEntity testEntityBack = getTestEntityRandomSampleGenerator();

        testCreator.addTestEntity(testEntityBack);
        assertThat(testCreator.getTestEntities()).containsOnly(testEntityBack);
        assertThat(testEntityBack.getTestCreator()).isEqualTo(testCreator);

        testCreator.removeTestEntity(testEntityBack);
        assertThat(testCreator.getTestEntities()).doesNotContain(testEntityBack);
        assertThat(testEntityBack.getTestCreator()).isNull();

        testCreator.testEntities(new HashSet<>(Set.of(testEntityBack)));
        assertThat(testCreator.getTestEntities()).containsOnly(testEntityBack);
        assertThat(testEntityBack.getTestCreator()).isEqualTo(testCreator);

        testCreator.setTestEntities(new HashSet<>());
        assertThat(testCreator.getTestEntities()).doesNotContain(testEntityBack);
        assertThat(testEntityBack.getTestCreator()).isNull();
    }
}
