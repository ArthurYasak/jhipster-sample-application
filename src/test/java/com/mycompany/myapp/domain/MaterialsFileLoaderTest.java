package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MaterialsFileLoaderTestSamples.*;
import static com.mycompany.myapp.domain.MaterialsFileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MaterialsFileLoaderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterialsFileLoader.class);
        MaterialsFileLoader materialsFileLoader1 = getMaterialsFileLoaderSample1();
        MaterialsFileLoader materialsFileLoader2 = new MaterialsFileLoader();
        assertThat(materialsFileLoader1).isNotEqualTo(materialsFileLoader2);

        materialsFileLoader2.setId(materialsFileLoader1.getId());
        assertThat(materialsFileLoader1).isEqualTo(materialsFileLoader2);

        materialsFileLoader2 = getMaterialsFileLoaderSample2();
        assertThat(materialsFileLoader1).isNotEqualTo(materialsFileLoader2);
    }

    @Test
    void materialsFileTest() throws Exception {
        MaterialsFileLoader materialsFileLoader = getMaterialsFileLoaderRandomSampleGenerator();
        MaterialsFile materialsFileBack = getMaterialsFileRandomSampleGenerator();

        materialsFileLoader.addMaterialsFile(materialsFileBack);
        assertThat(materialsFileLoader.getMaterialsFiles()).containsOnly(materialsFileBack);
        assertThat(materialsFileBack.getMaterialsFileLoader()).isEqualTo(materialsFileLoader);

        materialsFileLoader.removeMaterialsFile(materialsFileBack);
        assertThat(materialsFileLoader.getMaterialsFiles()).doesNotContain(materialsFileBack);
        assertThat(materialsFileBack.getMaterialsFileLoader()).isNull();

        materialsFileLoader.materialsFiles(new HashSet<>(Set.of(materialsFileBack)));
        assertThat(materialsFileLoader.getMaterialsFiles()).containsOnly(materialsFileBack);
        assertThat(materialsFileBack.getMaterialsFileLoader()).isEqualTo(materialsFileLoader);

        materialsFileLoader.setMaterialsFiles(new HashSet<>());
        assertThat(materialsFileLoader.getMaterialsFiles()).doesNotContain(materialsFileBack);
        assertThat(materialsFileBack.getMaterialsFileLoader()).isNull();
    }
}
