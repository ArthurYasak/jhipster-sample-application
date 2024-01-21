package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.TestCreatorTestSamples.*;
import static com.mycompany.myapp.domain.TestEntityTestSamples.*;
import static com.mycompany.myapp.domain.TestLoaderTestSamples.*;
import static com.mycompany.myapp.domain.TestUserTestSamples.*;
import static com.mycompany.myapp.domain.TesterTestSamples.*;
import static com.mycompany.myapp.domain.ThemeFileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TestEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestEntity.class);
        TestEntity testEntity1 = getTestEntitySample1();
        TestEntity testEntity2 = new TestEntity();
        assertThat(testEntity1).isNotEqualTo(testEntity2);

        testEntity2.setId(testEntity1.getId());
        assertThat(testEntity1).isEqualTo(testEntity2);

        testEntity2 = getTestEntitySample2();
        assertThat(testEntity1).isNotEqualTo(testEntity2);
    }

    @Test
    void themeFileTest() throws Exception {
        TestEntity testEntity = getTestEntityRandomSampleGenerator();
        ThemeFile themeFileBack = getThemeFileRandomSampleGenerator();

        testEntity.setThemeFile(themeFileBack);
        assertThat(testEntity.getThemeFile()).isEqualTo(themeFileBack);

        testEntity.themeFile(null);
        assertThat(testEntity.getThemeFile()).isNull();
    }

    @Test
    void testCreatorTest() throws Exception {
        TestEntity testEntity = getTestEntityRandomSampleGenerator();
        TestCreator testCreatorBack = getTestCreatorRandomSampleGenerator();

        testEntity.setTestCreator(testCreatorBack);
        assertThat(testEntity.getTestCreator()).isEqualTo(testCreatorBack);

        testEntity.testCreator(null);
        assertThat(testEntity.getTestCreator()).isNull();
    }

    @Test
    void testLoaderTest() throws Exception {
        TestEntity testEntity = getTestEntityRandomSampleGenerator();
        TestLoader testLoaderBack = getTestLoaderRandomSampleGenerator();

        testEntity.setTestLoader(testLoaderBack);
        assertThat(testEntity.getTestLoader()).isEqualTo(testLoaderBack);

        testEntity.testLoader(null);
        assertThat(testEntity.getTestLoader()).isNull();
    }

    @Test
    void testerTest() throws Exception {
        TestEntity testEntity = getTestEntityRandomSampleGenerator();
        Tester testerBack = getTesterRandomSampleGenerator();

        testEntity.setTester(testerBack);
        assertThat(testEntity.getTester()).isEqualTo(testerBack);

        testEntity.tester(null);
        assertThat(testEntity.getTester()).isNull();
    }

    @Test
    void testUserTest() throws Exception {
        TestEntity testEntity = getTestEntityRandomSampleGenerator();
        TestUser testUserBack = getTestUserRandomSampleGenerator();

        testEntity.addTestUser(testUserBack);
        assertThat(testEntity.getTestUsers()).containsOnly(testUserBack);

        testEntity.removeTestUser(testUserBack);
        assertThat(testEntity.getTestUsers()).doesNotContain(testUserBack);

        testEntity.testUsers(new HashSet<>(Set.of(testUserBack)));
        assertThat(testEntity.getTestUsers()).containsOnly(testUserBack);

        testEntity.setTestUsers(new HashSet<>());
        assertThat(testEntity.getTestUsers()).doesNotContain(testUserBack);
    }
}
