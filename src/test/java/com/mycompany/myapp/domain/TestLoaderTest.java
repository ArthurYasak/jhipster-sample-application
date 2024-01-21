package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.TestEntityTestSamples.*;
import static com.mycompany.myapp.domain.TestLoaderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TestLoaderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestLoader.class);
        TestLoader testLoader1 = getTestLoaderSample1();
        TestLoader testLoader2 = new TestLoader();
        assertThat(testLoader1).isNotEqualTo(testLoader2);

        testLoader2.setId(testLoader1.getId());
        assertThat(testLoader1).isEqualTo(testLoader2);

        testLoader2 = getTestLoaderSample2();
        assertThat(testLoader1).isNotEqualTo(testLoader2);
    }

    @Test
    void testEntityTest() throws Exception {
        TestLoader testLoader = getTestLoaderRandomSampleGenerator();
        TestEntity testEntityBack = getTestEntityRandomSampleGenerator();

        testLoader.addTestEntity(testEntityBack);
        assertThat(testLoader.getTestEntities()).containsOnly(testEntityBack);
        assertThat(testEntityBack.getTestLoader()).isEqualTo(testLoader);

        testLoader.removeTestEntity(testEntityBack);
        assertThat(testLoader.getTestEntities()).doesNotContain(testEntityBack);
        assertThat(testEntityBack.getTestLoader()).isNull();

        testLoader.testEntities(new HashSet<>(Set.of(testEntityBack)));
        assertThat(testLoader.getTestEntities()).containsOnly(testEntityBack);
        assertThat(testEntityBack.getTestLoader()).isEqualTo(testLoader);

        testLoader.setTestEntities(new HashSet<>());
        assertThat(testLoader.getTestEntities()).doesNotContain(testEntityBack);
        assertThat(testEntityBack.getTestLoader()).isNull();
    }
}
