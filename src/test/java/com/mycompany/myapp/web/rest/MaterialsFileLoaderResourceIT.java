package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.MaterialsFileLoader;
import com.mycompany.myapp.repository.MaterialsFileLoaderRepository;
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
 * Integration tests for the {@link MaterialsFileLoaderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MaterialsFileLoaderResourceIT {

    private static final Integer DEFAULT_FILES_AMOUNT = 1;
    private static final Integer UPDATED_FILES_AMOUNT = 2;

    private static final String ENTITY_API_URL = "/api/materials-file-loaders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MaterialsFileLoaderRepository materialsFileLoaderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaterialsFileLoaderMockMvc;

    private MaterialsFileLoader materialsFileLoader;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterialsFileLoader createEntity(EntityManager em) {
        MaterialsFileLoader materialsFileLoader = new MaterialsFileLoader().filesAmount(DEFAULT_FILES_AMOUNT);
        return materialsFileLoader;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterialsFileLoader createUpdatedEntity(EntityManager em) {
        MaterialsFileLoader materialsFileLoader = new MaterialsFileLoader().filesAmount(UPDATED_FILES_AMOUNT);
        return materialsFileLoader;
    }

    @BeforeEach
    public void initTest() {
        materialsFileLoader = createEntity(em);
    }

    @Test
    @Transactional
    void createMaterialsFileLoader() throws Exception {
        int databaseSizeBeforeCreate = materialsFileLoaderRepository.findAll().size();
        // Create the MaterialsFileLoader
        restMaterialsFileLoaderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(materialsFileLoader))
            )
            .andExpect(status().isCreated());

        // Validate the MaterialsFileLoader in the database
        List<MaterialsFileLoader> materialsFileLoaderList = materialsFileLoaderRepository.findAll();
        assertThat(materialsFileLoaderList).hasSize(databaseSizeBeforeCreate + 1);
        MaterialsFileLoader testMaterialsFileLoader = materialsFileLoaderList.get(materialsFileLoaderList.size() - 1);
        assertThat(testMaterialsFileLoader.getFilesAmount()).isEqualTo(DEFAULT_FILES_AMOUNT);
    }

    @Test
    @Transactional
    void createMaterialsFileLoaderWithExistingId() throws Exception {
        // Create the MaterialsFileLoader with an existing ID
        materialsFileLoader.setId(1L);

        int databaseSizeBeforeCreate = materialsFileLoaderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaterialsFileLoaderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(materialsFileLoader))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialsFileLoader in the database
        List<MaterialsFileLoader> materialsFileLoaderList = materialsFileLoaderRepository.findAll();
        assertThat(materialsFileLoaderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMaterialsFileLoaders() throws Exception {
        // Initialize the database
        materialsFileLoaderRepository.saveAndFlush(materialsFileLoader);

        // Get all the materialsFileLoaderList
        restMaterialsFileLoaderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materialsFileLoader.getId().intValue())))
            .andExpect(jsonPath("$.[*].filesAmount").value(hasItem(DEFAULT_FILES_AMOUNT)));
    }

    @Test
    @Transactional
    void getMaterialsFileLoader() throws Exception {
        // Initialize the database
        materialsFileLoaderRepository.saveAndFlush(materialsFileLoader);

        // Get the materialsFileLoader
        restMaterialsFileLoaderMockMvc
            .perform(get(ENTITY_API_URL_ID, materialsFileLoader.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(materialsFileLoader.getId().intValue()))
            .andExpect(jsonPath("$.filesAmount").value(DEFAULT_FILES_AMOUNT));
    }

    @Test
    @Transactional
    void getNonExistingMaterialsFileLoader() throws Exception {
        // Get the materialsFileLoader
        restMaterialsFileLoaderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMaterialsFileLoader() throws Exception {
        // Initialize the database
        materialsFileLoaderRepository.saveAndFlush(materialsFileLoader);

        int databaseSizeBeforeUpdate = materialsFileLoaderRepository.findAll().size();

        // Update the materialsFileLoader
        MaterialsFileLoader updatedMaterialsFileLoader = materialsFileLoaderRepository.findById(materialsFileLoader.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMaterialsFileLoader are not directly saved in db
        em.detach(updatedMaterialsFileLoader);
        updatedMaterialsFileLoader.filesAmount(UPDATED_FILES_AMOUNT);

        restMaterialsFileLoaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMaterialsFileLoader.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMaterialsFileLoader))
            )
            .andExpect(status().isOk());

        // Validate the MaterialsFileLoader in the database
        List<MaterialsFileLoader> materialsFileLoaderList = materialsFileLoaderRepository.findAll();
        assertThat(materialsFileLoaderList).hasSize(databaseSizeBeforeUpdate);
        MaterialsFileLoader testMaterialsFileLoader = materialsFileLoaderList.get(materialsFileLoaderList.size() - 1);
        assertThat(testMaterialsFileLoader.getFilesAmount()).isEqualTo(UPDATED_FILES_AMOUNT);
    }

    @Test
    @Transactional
    void putNonExistingMaterialsFileLoader() throws Exception {
        int databaseSizeBeforeUpdate = materialsFileLoaderRepository.findAll().size();
        materialsFileLoader.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialsFileLoaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materialsFileLoader.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(materialsFileLoader))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialsFileLoader in the database
        List<MaterialsFileLoader> materialsFileLoaderList = materialsFileLoaderRepository.findAll();
        assertThat(materialsFileLoaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMaterialsFileLoader() throws Exception {
        int databaseSizeBeforeUpdate = materialsFileLoaderRepository.findAll().size();
        materialsFileLoader.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialsFileLoaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(materialsFileLoader))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialsFileLoader in the database
        List<MaterialsFileLoader> materialsFileLoaderList = materialsFileLoaderRepository.findAll();
        assertThat(materialsFileLoaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMaterialsFileLoader() throws Exception {
        int databaseSizeBeforeUpdate = materialsFileLoaderRepository.findAll().size();
        materialsFileLoader.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialsFileLoaderMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(materialsFileLoader))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MaterialsFileLoader in the database
        List<MaterialsFileLoader> materialsFileLoaderList = materialsFileLoaderRepository.findAll();
        assertThat(materialsFileLoaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMaterialsFileLoaderWithPatch() throws Exception {
        // Initialize the database
        materialsFileLoaderRepository.saveAndFlush(materialsFileLoader);

        int databaseSizeBeforeUpdate = materialsFileLoaderRepository.findAll().size();

        // Update the materialsFileLoader using partial update
        MaterialsFileLoader partialUpdatedMaterialsFileLoader = new MaterialsFileLoader();
        partialUpdatedMaterialsFileLoader.setId(materialsFileLoader.getId());

        partialUpdatedMaterialsFileLoader.filesAmount(UPDATED_FILES_AMOUNT);

        restMaterialsFileLoaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaterialsFileLoader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMaterialsFileLoader))
            )
            .andExpect(status().isOk());

        // Validate the MaterialsFileLoader in the database
        List<MaterialsFileLoader> materialsFileLoaderList = materialsFileLoaderRepository.findAll();
        assertThat(materialsFileLoaderList).hasSize(databaseSizeBeforeUpdate);
        MaterialsFileLoader testMaterialsFileLoader = materialsFileLoaderList.get(materialsFileLoaderList.size() - 1);
        assertThat(testMaterialsFileLoader.getFilesAmount()).isEqualTo(UPDATED_FILES_AMOUNT);
    }

    @Test
    @Transactional
    void fullUpdateMaterialsFileLoaderWithPatch() throws Exception {
        // Initialize the database
        materialsFileLoaderRepository.saveAndFlush(materialsFileLoader);

        int databaseSizeBeforeUpdate = materialsFileLoaderRepository.findAll().size();

        // Update the materialsFileLoader using partial update
        MaterialsFileLoader partialUpdatedMaterialsFileLoader = new MaterialsFileLoader();
        partialUpdatedMaterialsFileLoader.setId(materialsFileLoader.getId());

        partialUpdatedMaterialsFileLoader.filesAmount(UPDATED_FILES_AMOUNT);

        restMaterialsFileLoaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaterialsFileLoader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMaterialsFileLoader))
            )
            .andExpect(status().isOk());

        // Validate the MaterialsFileLoader in the database
        List<MaterialsFileLoader> materialsFileLoaderList = materialsFileLoaderRepository.findAll();
        assertThat(materialsFileLoaderList).hasSize(databaseSizeBeforeUpdate);
        MaterialsFileLoader testMaterialsFileLoader = materialsFileLoaderList.get(materialsFileLoaderList.size() - 1);
        assertThat(testMaterialsFileLoader.getFilesAmount()).isEqualTo(UPDATED_FILES_AMOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingMaterialsFileLoader() throws Exception {
        int databaseSizeBeforeUpdate = materialsFileLoaderRepository.findAll().size();
        materialsFileLoader.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialsFileLoaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, materialsFileLoader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(materialsFileLoader))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialsFileLoader in the database
        List<MaterialsFileLoader> materialsFileLoaderList = materialsFileLoaderRepository.findAll();
        assertThat(materialsFileLoaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMaterialsFileLoader() throws Exception {
        int databaseSizeBeforeUpdate = materialsFileLoaderRepository.findAll().size();
        materialsFileLoader.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialsFileLoaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(materialsFileLoader))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialsFileLoader in the database
        List<MaterialsFileLoader> materialsFileLoaderList = materialsFileLoaderRepository.findAll();
        assertThat(materialsFileLoaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMaterialsFileLoader() throws Exception {
        int databaseSizeBeforeUpdate = materialsFileLoaderRepository.findAll().size();
        materialsFileLoader.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialsFileLoaderMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(materialsFileLoader))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MaterialsFileLoader in the database
        List<MaterialsFileLoader> materialsFileLoaderList = materialsFileLoaderRepository.findAll();
        assertThat(materialsFileLoaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMaterialsFileLoader() throws Exception {
        // Initialize the database
        materialsFileLoaderRepository.saveAndFlush(materialsFileLoader);

        int databaseSizeBeforeDelete = materialsFileLoaderRepository.findAll().size();

        // Delete the materialsFileLoader
        restMaterialsFileLoaderMockMvc
            .perform(delete(ENTITY_API_URL_ID, materialsFileLoader.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MaterialsFileLoader> materialsFileLoaderList = materialsFileLoaderRepository.findAll();
        assertThat(materialsFileLoaderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
