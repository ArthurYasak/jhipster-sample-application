package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ThemeFileCreator;
import com.mycompany.myapp.repository.ThemeFileCreatorRepository;
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
 * Integration tests for the {@link ThemeFileCreatorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ThemeFileCreatorResourceIT {

    private static final Integer DEFAULT_FILES_AMOUNT = 1;
    private static final Integer UPDATED_FILES_AMOUNT = 2;

    private static final String ENTITY_API_URL = "/api/theme-file-creators";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ThemeFileCreatorRepository themeFileCreatorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restThemeFileCreatorMockMvc;

    private ThemeFileCreator themeFileCreator;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ThemeFileCreator createEntity(EntityManager em) {
        ThemeFileCreator themeFileCreator = new ThemeFileCreator().filesAmount(DEFAULT_FILES_AMOUNT);
        return themeFileCreator;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ThemeFileCreator createUpdatedEntity(EntityManager em) {
        ThemeFileCreator themeFileCreator = new ThemeFileCreator().filesAmount(UPDATED_FILES_AMOUNT);
        return themeFileCreator;
    }

    @BeforeEach
    public void initTest() {
        themeFileCreator = createEntity(em);
    }

    @Test
    @Transactional
    void createThemeFileCreator() throws Exception {
        int databaseSizeBeforeCreate = themeFileCreatorRepository.findAll().size();
        // Create the ThemeFileCreator
        restThemeFileCreatorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(themeFileCreator))
            )
            .andExpect(status().isCreated());

        // Validate the ThemeFileCreator in the database
        List<ThemeFileCreator> themeFileCreatorList = themeFileCreatorRepository.findAll();
        assertThat(themeFileCreatorList).hasSize(databaseSizeBeforeCreate + 1);
        ThemeFileCreator testThemeFileCreator = themeFileCreatorList.get(themeFileCreatorList.size() - 1);
        assertThat(testThemeFileCreator.getFilesAmount()).isEqualTo(DEFAULT_FILES_AMOUNT);
    }

    @Test
    @Transactional
    void createThemeFileCreatorWithExistingId() throws Exception {
        // Create the ThemeFileCreator with an existing ID
        themeFileCreator.setId(1L);

        int databaseSizeBeforeCreate = themeFileCreatorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restThemeFileCreatorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(themeFileCreator))
            )
            .andExpect(status().isBadRequest());

        // Validate the ThemeFileCreator in the database
        List<ThemeFileCreator> themeFileCreatorList = themeFileCreatorRepository.findAll();
        assertThat(themeFileCreatorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllThemeFileCreators() throws Exception {
        // Initialize the database
        themeFileCreatorRepository.saveAndFlush(themeFileCreator);

        // Get all the themeFileCreatorList
        restThemeFileCreatorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(themeFileCreator.getId().intValue())))
            .andExpect(jsonPath("$.[*].filesAmount").value(hasItem(DEFAULT_FILES_AMOUNT)));
    }

    @Test
    @Transactional
    void getThemeFileCreator() throws Exception {
        // Initialize the database
        themeFileCreatorRepository.saveAndFlush(themeFileCreator);

        // Get the themeFileCreator
        restThemeFileCreatorMockMvc
            .perform(get(ENTITY_API_URL_ID, themeFileCreator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(themeFileCreator.getId().intValue()))
            .andExpect(jsonPath("$.filesAmount").value(DEFAULT_FILES_AMOUNT));
    }

    @Test
    @Transactional
    void getNonExistingThemeFileCreator() throws Exception {
        // Get the themeFileCreator
        restThemeFileCreatorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingThemeFileCreator() throws Exception {
        // Initialize the database
        themeFileCreatorRepository.saveAndFlush(themeFileCreator);

        int databaseSizeBeforeUpdate = themeFileCreatorRepository.findAll().size();

        // Update the themeFileCreator
        ThemeFileCreator updatedThemeFileCreator = themeFileCreatorRepository.findById(themeFileCreator.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedThemeFileCreator are not directly saved in db
        em.detach(updatedThemeFileCreator);
        updatedThemeFileCreator.filesAmount(UPDATED_FILES_AMOUNT);

        restThemeFileCreatorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedThemeFileCreator.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedThemeFileCreator))
            )
            .andExpect(status().isOk());

        // Validate the ThemeFileCreator in the database
        List<ThemeFileCreator> themeFileCreatorList = themeFileCreatorRepository.findAll();
        assertThat(themeFileCreatorList).hasSize(databaseSizeBeforeUpdate);
        ThemeFileCreator testThemeFileCreator = themeFileCreatorList.get(themeFileCreatorList.size() - 1);
        assertThat(testThemeFileCreator.getFilesAmount()).isEqualTo(UPDATED_FILES_AMOUNT);
    }

    @Test
    @Transactional
    void putNonExistingThemeFileCreator() throws Exception {
        int databaseSizeBeforeUpdate = themeFileCreatorRepository.findAll().size();
        themeFileCreator.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restThemeFileCreatorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, themeFileCreator.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(themeFileCreator))
            )
            .andExpect(status().isBadRequest());

        // Validate the ThemeFileCreator in the database
        List<ThemeFileCreator> themeFileCreatorList = themeFileCreatorRepository.findAll();
        assertThat(themeFileCreatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchThemeFileCreator() throws Exception {
        int databaseSizeBeforeUpdate = themeFileCreatorRepository.findAll().size();
        themeFileCreator.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThemeFileCreatorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(themeFileCreator))
            )
            .andExpect(status().isBadRequest());

        // Validate the ThemeFileCreator in the database
        List<ThemeFileCreator> themeFileCreatorList = themeFileCreatorRepository.findAll();
        assertThat(themeFileCreatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamThemeFileCreator() throws Exception {
        int databaseSizeBeforeUpdate = themeFileCreatorRepository.findAll().size();
        themeFileCreator.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThemeFileCreatorMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(themeFileCreator))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ThemeFileCreator in the database
        List<ThemeFileCreator> themeFileCreatorList = themeFileCreatorRepository.findAll();
        assertThat(themeFileCreatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateThemeFileCreatorWithPatch() throws Exception {
        // Initialize the database
        themeFileCreatorRepository.saveAndFlush(themeFileCreator);

        int databaseSizeBeforeUpdate = themeFileCreatorRepository.findAll().size();

        // Update the themeFileCreator using partial update
        ThemeFileCreator partialUpdatedThemeFileCreator = new ThemeFileCreator();
        partialUpdatedThemeFileCreator.setId(themeFileCreator.getId());

        partialUpdatedThemeFileCreator.filesAmount(UPDATED_FILES_AMOUNT);

        restThemeFileCreatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedThemeFileCreator.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedThemeFileCreator))
            )
            .andExpect(status().isOk());

        // Validate the ThemeFileCreator in the database
        List<ThemeFileCreator> themeFileCreatorList = themeFileCreatorRepository.findAll();
        assertThat(themeFileCreatorList).hasSize(databaseSizeBeforeUpdate);
        ThemeFileCreator testThemeFileCreator = themeFileCreatorList.get(themeFileCreatorList.size() - 1);
        assertThat(testThemeFileCreator.getFilesAmount()).isEqualTo(UPDATED_FILES_AMOUNT);
    }

    @Test
    @Transactional
    void fullUpdateThemeFileCreatorWithPatch() throws Exception {
        // Initialize the database
        themeFileCreatorRepository.saveAndFlush(themeFileCreator);

        int databaseSizeBeforeUpdate = themeFileCreatorRepository.findAll().size();

        // Update the themeFileCreator using partial update
        ThemeFileCreator partialUpdatedThemeFileCreator = new ThemeFileCreator();
        partialUpdatedThemeFileCreator.setId(themeFileCreator.getId());

        partialUpdatedThemeFileCreator.filesAmount(UPDATED_FILES_AMOUNT);

        restThemeFileCreatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedThemeFileCreator.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedThemeFileCreator))
            )
            .andExpect(status().isOk());

        // Validate the ThemeFileCreator in the database
        List<ThemeFileCreator> themeFileCreatorList = themeFileCreatorRepository.findAll();
        assertThat(themeFileCreatorList).hasSize(databaseSizeBeforeUpdate);
        ThemeFileCreator testThemeFileCreator = themeFileCreatorList.get(themeFileCreatorList.size() - 1);
        assertThat(testThemeFileCreator.getFilesAmount()).isEqualTo(UPDATED_FILES_AMOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingThemeFileCreator() throws Exception {
        int databaseSizeBeforeUpdate = themeFileCreatorRepository.findAll().size();
        themeFileCreator.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restThemeFileCreatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, themeFileCreator.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(themeFileCreator))
            )
            .andExpect(status().isBadRequest());

        // Validate the ThemeFileCreator in the database
        List<ThemeFileCreator> themeFileCreatorList = themeFileCreatorRepository.findAll();
        assertThat(themeFileCreatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchThemeFileCreator() throws Exception {
        int databaseSizeBeforeUpdate = themeFileCreatorRepository.findAll().size();
        themeFileCreator.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThemeFileCreatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(themeFileCreator))
            )
            .andExpect(status().isBadRequest());

        // Validate the ThemeFileCreator in the database
        List<ThemeFileCreator> themeFileCreatorList = themeFileCreatorRepository.findAll();
        assertThat(themeFileCreatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamThemeFileCreator() throws Exception {
        int databaseSizeBeforeUpdate = themeFileCreatorRepository.findAll().size();
        themeFileCreator.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThemeFileCreatorMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(themeFileCreator))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ThemeFileCreator in the database
        List<ThemeFileCreator> themeFileCreatorList = themeFileCreatorRepository.findAll();
        assertThat(themeFileCreatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteThemeFileCreator() throws Exception {
        // Initialize the database
        themeFileCreatorRepository.saveAndFlush(themeFileCreator);

        int databaseSizeBeforeDelete = themeFileCreatorRepository.findAll().size();

        // Delete the themeFileCreator
        restThemeFileCreatorMockMvc
            .perform(delete(ENTITY_API_URL_ID, themeFileCreator.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ThemeFileCreator> themeFileCreatorList = themeFileCreatorRepository.findAll();
        assertThat(themeFileCreatorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
