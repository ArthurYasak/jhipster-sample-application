package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MaterialsFileLoaderTestSamples.*;
import static com.mycompany.myapp.domain.MaterialsFileTestSamples.*;
import static com.mycompany.myapp.domain.ThemeFileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaterialsFileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterialsFile.class);
        MaterialsFile materialsFile1 = getMaterialsFileSample1();
        MaterialsFile materialsFile2 = new MaterialsFile();
        assertThat(materialsFile1).isNotEqualTo(materialsFile2);

        materialsFile2.setId(materialsFile1.getId());
        assertThat(materialsFile1).isEqualTo(materialsFile2);

        materialsFile2 = getMaterialsFileSample2();
        assertThat(materialsFile1).isNotEqualTo(materialsFile2);
    }

    @Test
    void materialsFileLoaderTest() throws Exception {
        MaterialsFile materialsFile = getMaterialsFileRandomSampleGenerator();
        MaterialsFileLoader materialsFileLoaderBack = getMaterialsFileLoaderRandomSampleGenerator();

        materialsFile.setMaterialsFileLoader(materialsFileLoaderBack);
        assertThat(materialsFile.getMaterialsFileLoader()).isEqualTo(materialsFileLoaderBack);

        materialsFile.materialsFileLoader(null);
        assertThat(materialsFile.getMaterialsFileLoader()).isNull();
    }

    @Test
    void themeFileTest() throws Exception {
        MaterialsFile materialsFile = getMaterialsFileRandomSampleGenerator();
        ThemeFile themeFileBack = getThemeFileRandomSampleGenerator();

        materialsFile.setThemeFile(themeFileBack);
        assertThat(materialsFile.getThemeFile()).isEqualTo(themeFileBack);
        assertThat(themeFileBack.getMaterialsFile()).isEqualTo(materialsFile);

        materialsFile.themeFile(null);
        assertThat(materialsFile.getThemeFile()).isNull();
        assertThat(themeFileBack.getMaterialsFile()).isNull();
    }
}
