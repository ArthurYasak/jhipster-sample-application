package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.MaterialsFile;
import com.mycompany.myapp.repository.MaterialsFileRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MaterialsFileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MaterialsFileResourceIT {

    private static final String DEFAULT_MATERIALS = "AAAAAAAAAA";
    private static final String UPDATED_MATERIALS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/materials-files";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MaterialsFileRepository materialsFileRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaterialsFileMockMvc;

    private MaterialsFile materialsFile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterialsFile createEntity(EntityManager em) {
        MaterialsFile materialsFile = new MaterialsFile().materials(DEFAULT_MATERIALS);
        return materialsFile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterialsFile createUpdatedEntity(EntityManager em) {
        MaterialsFile materialsFile = new MaterialsFile().materials(UPDATED_MATERIALS);
        return materialsFile;
    }

    @BeforeEach
    public void initTest() {
        materialsFile = createEntity(em);
    }

    @Test
    @Transactional
    void createMaterialsFile() throws Exception {
        int databaseSizeBeforeCreate = materialsFileRepository.findAll().size();
        // Create the MaterialsFile
        restMaterialsFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(materialsFile)))
            .andExpect(status().isCreated());

        // Validate the MaterialsFile in the database
        List<MaterialsFile> materialsFileList = materialsFileRepository.findAll();
        assertThat(materialsFileList).hasSize(databaseSizeBeforeCreate + 1);
        MaterialsFile testMaterialsFile = materialsFileList.get(materialsFileList.size() - 1);
        assertThat(testMaterialsFile.getMaterials()).isEqualTo(DEFAULT_MATERIALS);
    }

    @Test
    @Transactional
    void createMaterialsFileWithExistingId() throws Exception {
        // Create the MaterialsFile with an existing ID
        materialsFile.setId(1L);

        int databaseSizeBeforeCreate = materialsFileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaterialsFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(materialsFile)))
            .andExpect(status().isBadRequest());

        // Validate the MaterialsFile in the database
        List<MaterialsFile> materialsFileList = materialsFileRepository.findAll();
        assertThat(materialsFileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMaterialsFiles() throws Exception {
        // Initialize the database
        materialsFileRepository.saveAndFlush(materialsFile);

        // Get all the materialsFileList
        restMaterialsFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materialsFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].materials").value(hasItem(DEFAULT_MATERIALS)));
    }

    @Test
    @Transactional
    void getMaterialsFile() throws Exception {
        // Initialize the database
        materialsFileRepository.saveAndFlush(materialsFile);

        // Get the materialsFile
        restMaterialsFileMockMvc
            .perform(get(ENTITY_API_URL_ID, materialsFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(materialsFile.getId().intValue()))
            .andExpect(jsonPath("$.materials").value(DEFAULT_MATERIALS));
    }

    @Test
    @Transactional
    void getNonExistingMaterialsFile() throws Exception {
        // Get the materialsFile
        restMaterialsFileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMaterialsFile() throws Exception {
        // Initialize the database
        materialsFileRepository.saveAndFlush(materialsFile);

        int databaseSizeBeforeUpdate = materialsFileRepository.findAll().size();

        // Update the materialsFile
        MaterialsFile updatedMaterialsFile = materialsFileRepository.findById(materialsFile.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMaterialsFile are not directly saved in db
        em.detach(updatedMaterialsFile);
        updatedMaterialsFile.materials(UPDATED_MATERIALS);

        restMaterialsFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMaterialsFile.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMaterialsFile))
            )
            .andExpect(status().isOk());

        // Validate the MaterialsFile in the database
        List<MaterialsFile> materialsFileList = materialsFileRepository.findAll();
        assertThat(materialsFileList).hasSize(databaseSizeBeforeUpdate);
        MaterialsFile testMaterialsFile = materialsFileList.get(materialsFileList.size() - 1);
        assertThat(testMaterialsFile.getMaterials()).isEqualTo(UPDATED_MATERIALS);
    }

    @Test
    @Transactional
    void putNonExistingMaterialsFile() throws Exception {
        int databaseSizeBeforeUpdate = materialsFileRepository.findAll().size();
        materialsFile.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialsFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materialsFile.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(materialsFile))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialsFile in the database
        List<MaterialsFile> materialsFileList = materialsFileRepository.findAll();
        assertThat(materialsFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMaterialsFile() throws Exception {
        int databaseSizeBeforeUpdate = materialsFileRepository.findAll().size();
        materialsFile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialsFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(materialsFile))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialsFile in the database
        List<MaterialsFile> materialsFileList = materialsFileRepository.findAll();
        assertThat(materialsFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMaterialsFile() throws Exception {
        int databaseSizeBeforeUpdate = materialsFileRepository.findAll().size();
        materialsFile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialsFileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(materialsFile)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MaterialsFile in the database
        List<MaterialsFile> materialsFileList = materialsFileRepository.findAll();
        assertThat(materialsFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMaterialsFileWithPatch() throws Exception {
        // Initialize the database
        materialsFileRepository.saveAndFlush(materialsFile);

        int databaseSizeBeforeUpdate = materialsFileRepository.findAll().size();

        // Update the materialsFile using partial update
        MaterialsFile partialUpdatedMaterialsFile = new MaterialsFile();
        partialUpdatedMaterialsFile.setId(materialsFile.getId());

        partialUpdatedMaterialsFile.materials(UPDATED_MATERIALS);

        restMaterialsFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaterialsFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMaterialsFile))
            )
            .andExpect(status().isOk());

        // Validate the MaterialsFile in the database
        List<MaterialsFile> materialsFileList = materialsFileRepository.findAll();
        assertThat(materialsFileList).hasSize(databaseSizeBeforeUpdate);
        MaterialsFile testMaterialsFile = materialsFileList.get(materialsFileList.size() - 1);
        assertThat(testMaterialsFile.getMaterials()).isEqualTo(UPDATED_MATERIALS);
    }

    @Test
    @Transactional
    void fullUpdateMaterialsFileWithPatch() throws Exception {
        // Initialize the database
        materialsFileRepository.saveAndFlush(materialsFile);

        int databaseSizeBeforeUpdate = materialsFileRepository.findAll().size();

        // Update the materialsFile using partial update
        MaterialsFile partialUpdatedMaterialsFile = new MaterialsFile();
        partialUpdatedMaterialsFile.setId(materialsFile.getId());

        partialUpdatedMaterialsFile.materials(UPDATED_MATERIALS);

        restMaterialsFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaterialsFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMaterialsFile))
            )
            .andExpect(status().isOk());

        // Validate the MaterialsFile in the database
        List<MaterialsFile> materialsFileList = materialsFileRepository.findAll();
        assertThat(materialsFileList).hasSize(databaseSizeBeforeUpdate);
        MaterialsFile testMaterialsFile = materialsFileList.get(materialsFileList.size() - 1);
        assertThat(testMaterialsFile.getMaterials()).isEqualTo(UPDATED_MATERIALS);
    }

    @Test
    @Transactional
    void patchNonExistingMaterialsFile() throws Exception {
        int databaseSizeBeforeUpdate = materialsFileRepository.findAll().size();
        materialsFile.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialsFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, materialsFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(materialsFile))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialsFile in the database
        List<MaterialsFile> materialsFileList = materialsFileRepository.findAll();
        assertThat(materialsFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMaterialsFile() throws Exception {
        int databaseSizeBeforeUpdate = materialsFileRepository.findAll().size();
        materialsFile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialsFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(materialsFile))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialsFile in the database
        List<MaterialsFile> materialsFileList = materialsFileRepository.findAll();
        assertThat(materialsFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMaterialsFile() throws Exception {
        int databaseSizeBeforeUpdate = materialsFileRepository.findAll().size();
        materialsFile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialsFileMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(materialsFile))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MaterialsFile in the database
        List<MaterialsFile> materialsFileList = materialsFileRepository.findAll();
        assertThat(materialsFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMaterialsFile() throws Exception {
        // Initialize the database
        materialsFileRepository.saveAndFlush(materialsFile);

        int databaseSizeBeforeDelete = materialsFileRepository.findAll().size();

        // Delete the materialsFile
        restMaterialsFileMockMvc
            .perform(delete(ENTITY_API_URL_ID, materialsFile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MaterialsFile> materialsFileList = materialsFileRepository.findAll();
        assertThat(materialsFileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
