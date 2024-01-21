package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ThemeFile;
import com.mycompany.myapp.repository.ThemeFileRepository;
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
 * Integration tests for the {@link ThemeFileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ThemeFileResourceIT {

    private static final String DEFAULT_THEME = "AAAAAAAAAA";
    private static final String UPDATED_THEME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/theme-files";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ThemeFileRepository themeFileRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restThemeFileMockMvc;

    private ThemeFile themeFile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ThemeFile createEntity(EntityManager em) {
        ThemeFile themeFile = new ThemeFile().theme(DEFAULT_THEME);
        return themeFile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ThemeFile createUpdatedEntity(EntityManager em) {
        ThemeFile themeFile = new ThemeFile().theme(UPDATED_THEME);
        return themeFile;
    }

    @BeforeEach
    public void initTest() {
        themeFile = createEntity(em);
    }

    @Test
    @Transactional
    void createThemeFile() throws Exception {
        int databaseSizeBeforeCreate = themeFileRepository.findAll().size();
        // Create the ThemeFile
        restThemeFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(themeFile)))
            .andExpect(status().isCreated());

        // Validate the ThemeFile in the database
        List<ThemeFile> themeFileList = themeFileRepository.findAll();
        assertThat(themeFileList).hasSize(databaseSizeBeforeCreate + 1);
        ThemeFile testThemeFile = themeFileList.get(themeFileList.size() - 1);
        assertThat(testThemeFile.getTheme()).isEqualTo(DEFAULT_THEME);
    }

    @Test
    @Transactional
    void createThemeFileWithExistingId() throws Exception {
        // Create the ThemeFile with an existing ID
        themeFile.setId(1L);

        int databaseSizeBeforeCreate = themeFileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restThemeFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(themeFile)))
            .andExpect(status().isBadRequest());

        // Validate the ThemeFile in the database
        List<ThemeFile> themeFileList = themeFileRepository.findAll();
        assertThat(themeFileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllThemeFiles() throws Exception {
        // Initialize the database
        themeFileRepository.saveAndFlush(themeFile);

        // Get all the themeFileList
        restThemeFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(themeFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].theme").value(hasItem(DEFAULT_THEME)));
    }

    @Test
    @Transactional
    void getThemeFile() throws Exception {
        // Initialize the database
        themeFileRepository.saveAndFlush(themeFile);

        // Get the themeFile
        restThemeFileMockMvc
            .perform(get(ENTITY_API_URL_ID, themeFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(themeFile.getId().intValue()))
            .andExpect(jsonPath("$.theme").value(DEFAULT_THEME));
    }

    @Test
    @Transactional
    void getNonExistingThemeFile() throws Exception {
        // Get the themeFile
        restThemeFileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingThemeFile() throws Exception {
        // Initialize the database
        themeFileRepository.saveAndFlush(themeFile);

        int databaseSizeBeforeUpdate = themeFileRepository.findAll().size();

        // Update the themeFile
        ThemeFile updatedThemeFile = themeFileRepository.findById(themeFile.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedThemeFile are not directly saved in db
        em.detach(updatedThemeFile);
        updatedThemeFile.theme(UPDATED_THEME);

        restThemeFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedThemeFile.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedThemeFile))
            )
            .andExpect(status().isOk());

        // Validate the ThemeFile in the database
        List<ThemeFile> themeFileList = themeFileRepository.findAll();
        assertThat(themeFileList).hasSize(databaseSizeBeforeUpdate);
        ThemeFile testThemeFile = themeFileList.get(themeFileList.size() - 1);
        assertThat(testThemeFile.getTheme()).isEqualTo(UPDATED_THEME);
    }

    @Test
    @Transactional
    void putNonExistingThemeFile() throws Exception {
        int databaseSizeBeforeUpdate = themeFileRepository.findAll().size();
        themeFile.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restThemeFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, themeFile.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(themeFile))
            )
            .andExpect(status().isBadRequest());

        // Validate the ThemeFile in the database
        List<ThemeFile> themeFileList = themeFileRepository.findAll();
        assertThat(themeFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchThemeFile() throws Exception {
        int databaseSizeBeforeUpdate = themeFileRepository.findAll().size();
        themeFile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThemeFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(themeFile))
            )
            .andExpect(status().isBadRequest());

        // Validate the ThemeFile in the database
        List<ThemeFile> themeFileList = themeFileRepository.findAll();
        assertThat(themeFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamThemeFile() throws Exception {
        int databaseSizeBeforeUpdate = themeFileRepository.findAll().size();
        themeFile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThemeFileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(themeFile)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ThemeFile in the database
        List<ThemeFile> themeFileList = themeFileRepository.findAll();
        assertThat(themeFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateThemeFileWithPatch() throws Exception {
        // Initialize the database
        themeFileRepository.saveAndFlush(themeFile);

        int databaseSizeBeforeUpdate = themeFileRepository.findAll().size();

        // Update the themeFile using partial update
        ThemeFile partialUpdatedThemeFile = new ThemeFile();
        partialUpdatedThemeFile.setId(themeFile.getId());

        restThemeFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedThemeFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedThemeFile))
            )
            .andExpect(status().isOk());

        // Validate the ThemeFile in the database
        List<ThemeFile> themeFileList = themeFileRepository.findAll();
        assertThat(themeFileList).hasSize(databaseSizeBeforeUpdate);
        ThemeFile testThemeFile = themeFileList.get(themeFileList.size() - 1);
        assertThat(testThemeFile.getTheme()).isEqualTo(DEFAULT_THEME);
    }

    @Test
    @Transactional
    void fullUpdateThemeFileWithPatch() throws Exception {
        // Initialize the database
        themeFileRepository.saveAndFlush(themeFile);

        int databaseSizeBeforeUpdate = themeFileRepository.findAll().size();

        // Update the themeFile using partial update
        ThemeFile partialUpdatedThemeFile = new ThemeFile();
        partialUpdatedThemeFile.setId(themeFile.getId());

        partialUpdatedThemeFile.theme(UPDATED_THEME);

        restThemeFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedThemeFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedThemeFile))
            )
            .andExpect(status().isOk());

        // Validate the ThemeFile in the database
        List<ThemeFile> themeFileList = themeFileRepository.findAll();
        assertThat(themeFileList).hasSize(databaseSizeBeforeUpdate);
        ThemeFile testThemeFile = themeFileList.get(themeFileList.size() - 1);
        assertThat(testThemeFile.getTheme()).isEqualTo(UPDATED_THEME);
    }

    @Test
    @Transactional
    void patchNonExistingThemeFile() throws Exception {
        int databaseSizeBeforeUpdate = themeFileRepository.findAll().size();
        themeFile.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restThemeFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, themeFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(themeFile))
            )
            .andExpect(status().isBadRequest());

        // Validate the ThemeFile in the database
        List<ThemeFile> themeFileList = themeFileRepository.findAll();
        assertThat(themeFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchThemeFile() throws Exception {
        int databaseSizeBeforeUpdate = themeFileRepository.findAll().size();
        themeFile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThemeFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(themeFile))
            )
            .andExpect(status().isBadRequest());

        // Validate the ThemeFile in the database
        List<ThemeFile> themeFileList = themeFileRepository.findAll();
        assertThat(themeFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamThemeFile() throws Exception {
        int databaseSizeBeforeUpdate = themeFileRepository.findAll().size();
        themeFile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThemeFileMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(themeFile))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ThemeFile in the database
        List<ThemeFile> themeFileList = themeFileRepository.findAll();
        assertThat(themeFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteThemeFile() throws Exception {
        // Initialize the database
        themeFileRepository.saveAndFlush(themeFile);

        int databaseSizeBeforeDelete = themeFileRepository.findAll().size();

        // Delete the themeFile
        restThemeFileMockMvc
            .perform(delete(ENTITY_API_URL_ID, themeFile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ThemeFile> themeFileList = themeFileRepository.findAll();
        assertThat(themeFileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
