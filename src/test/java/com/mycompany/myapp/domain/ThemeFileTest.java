package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MaterialsFileTestSamples.*;
import static com.mycompany.myapp.domain.TestEntityTestSamples.*;
import static com.mycompany.myapp.domain.ThemeFileCreatorTestSamples.*;
import static com.mycompany.myapp.domain.ThemeFileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ThemeFileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ThemeFile.class);
        ThemeFile themeFile1 = getThemeFileSample1();
        ThemeFile themeFile2 = new ThemeFile();
        assertThat(themeFile1).isNotEqualTo(themeFile2);

        themeFile2.setId(themeFile1.getId());
        assertThat(themeFile1).isEqualTo(themeFile2);

        themeFile2 = getThemeFileSample2();
        assertThat(themeFile1).isNotEqualTo(themeFile2);
    }

    @Test
    void materialsFileTest() throws Exception {
        ThemeFile themeFile = getThemeFileRandomSampleGenerator();
        MaterialsFile materialsFileBack = getMaterialsFileRandomSampleGenerator();

        themeFile.setMaterialsFile(materialsFileBack);
        assertThat(themeFile.getMaterialsFile()).isEqualTo(materialsFileBack);

        themeFile.materialsFile(null);
        assertThat(themeFile.getMaterialsFile()).isNull();
    }

    @Test
    void themeFileCreatorTest() throws Exception {
        ThemeFile themeFile = getThemeFileRandomSampleGenerator();
        ThemeFileCreator themeFileCreatorBack = getThemeFileCreatorRandomSampleGenerator();

        themeFile.setThemeFileCreator(themeFileCreatorBack);
        assertThat(themeFile.getThemeFileCreator()).isEqualTo(themeFileCreatorBack);

        themeFile.themeFileCreator(null);
        assertThat(themeFile.getThemeFileCreator()).isNull();
    }

    @Test
    void testEntityTest() throws Exception {
        ThemeFile themeFile = getThemeFileRandomSampleGenerator();
        TestEntity testEntityBack = getTestEntityRandomSampleGenerator();

        themeFile.setTestEntity(testEntityBack);
        assertThat(themeFile.getTestEntity()).isEqualTo(testEntityBack);
        assertThat(testEntityBack.getThemeFile()).isEqualTo(themeFile);

        themeFile.testEntity(null);
        assertThat(themeFile.getTestEntity()).isNull();
        assertThat(testEntityBack.getThemeFile()).isNull();
    }
}
