package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ThemeFileCreatorTestSamples.*;
import static com.mycompany.myapp.domain.ThemeFileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ThemeFileCreatorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ThemeFileCreator.class);
        ThemeFileCreator themeFileCreator1 = getThemeFileCreatorSample1();
        ThemeFileCreator themeFileCreator2 = new ThemeFileCreator();
        assertThat(themeFileCreator1).isNotEqualTo(themeFileCreator2);

        themeFileCreator2.setId(themeFileCreator1.getId());
        assertThat(themeFileCreator1).isEqualTo(themeFileCreator2);

        themeFileCreator2 = getThemeFileCreatorSample2();
        assertThat(themeFileCreator1).isNotEqualTo(themeFileCreator2);
    }

    @Test
    void themeFileTest() throws Exception {
        ThemeFileCreator themeFileCreator = getThemeFileCreatorRandomSampleGenerator();
        ThemeFile themeFileBack = getThemeFileRandomSampleGenerator();

        themeFileCreator.addThemeFile(themeFileBack);
        assertThat(themeFileCreator.getThemeFiles()).containsOnly(themeFileBack);
        assertThat(themeFileBack.getThemeFileCreator()).isEqualTo(themeFileCreator);

        themeFileCreator.removeThemeFile(themeFileBack);
        assertThat(themeFileCreator.getThemeFiles()).doesNotContain(themeFileBack);
        assertThat(themeFileBack.getThemeFileCreator()).isNull();

        themeFileCreator.themeFiles(new HashSet<>(Set.of(themeFileBack)));
        assertThat(themeFileCreator.getThemeFiles()).containsOnly(themeFileBack);
        assertThat(themeFileBack.getThemeFileCreator()).isEqualTo(themeFileCreator);

        themeFileCreator.setThemeFiles(new HashSet<>());
        assertThat(themeFileCreator.getThemeFiles()).doesNotContain(themeFileBack);
        assertThat(themeFileBack.getThemeFileCreator()).isNull();
    }
}
