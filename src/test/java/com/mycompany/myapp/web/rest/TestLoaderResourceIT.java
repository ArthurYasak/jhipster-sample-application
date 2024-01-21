package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.TestLoader;
import com.mycompany.myapp.repository.TestLoaderRepository;
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
 * Integration tests for the {@link TestLoaderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestLoaderResourceIT {

    private static final Integer DEFAULT_TESTS_AMOUNT = 1;
    private static final Integer UPDATED_TESTS_AMOUNT = 2;

    private static final String ENTITY_API_URL = "/api/test-loaders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestLoaderRepository testLoaderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestLoaderMockMvc;

    private TestLoader testLoader;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestLoader createEntity(EntityManager em) {
        TestLoader testLoader = new TestLoader().testsAmount(DEFAULT_TESTS_AMOUNT);
        return testLoader;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestLoader createUpdatedEntity(EntityManager em) {
        TestLoader testLoader = new TestLoader().testsAmount(UPDATED_TESTS_AMOUNT);
        return testLoader;
    }

    @BeforeEach
    public void initTest() {
        testLoader = createEntity(em);
    }

    @Test
    @Transactional
    void createTestLoader() throws Exception {
        int databaseSizeBeforeCreate = testLoaderRepository.findAll().size();
        // Create the TestLoader
        restTestLoaderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testLoader)))
            .andExpect(status().isCreated());

        // Validate the TestLoader in the database
        List<TestLoader> testLoaderList = testLoaderRepository.findAll();
        assertThat(testLoaderList).hasSize(databaseSizeBeforeCreate + 1);
        TestLoader testTestLoader = testLoaderList.get(testLoaderList.size() - 1);
        assertThat(testTestLoader.getTestsAmount()).isEqualTo(DEFAULT_TESTS_AMOUNT);
    }

    @Test
    @Transactional
    void createTestLoaderWithExistingId() throws Exception {
        // Create the TestLoader with an existing ID
        testLoader.setId(1L);

        int databaseSizeBeforeCreate = testLoaderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestLoaderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testLoader)))
            .andExpect(status().isBadRequest());

        // Validate the TestLoader in the database
        List<TestLoader> testLoaderList = testLoaderRepository.findAll();
        assertThat(testLoaderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestLoaders() throws Exception {
        // Initialize the database
        testLoaderRepository.saveAndFlush(testLoader);

        // Get all the testLoaderList
        restTestLoaderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testLoader.getId().intValue())))
            .andExpect(jsonPath("$.[*].testsAmount").value(hasItem(DEFAULT_TESTS_AMOUNT)));
    }

    @Test
    @Transactional
    void getTestLoader() throws Exception {
        // Initialize the database
        testLoaderRepository.saveAndFlush(testLoader);

        // Get the testLoader
        restTestLoaderMockMvc
            .perform(get(ENTITY_API_URL_ID, testLoader.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testLoader.getId().intValue()))
            .andExpect(jsonPath("$.testsAmount").value(DEFAULT_TESTS_AMOUNT));
    }

    @Test
    @Transactional
    void getNonExistingTestLoader() throws Exception {
        // Get the testLoader
        restTestLoaderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestLoader() throws Exception {
        // Initialize the database
        testLoaderRepository.saveAndFlush(testLoader);

        int databaseSizeBeforeUpdate = testLoaderRepository.findAll().size();

        // Update the testLoader
        TestLoader updatedTestLoader = testLoaderRepository.findById(testLoader.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTestLoader are not directly saved in db
        em.detach(updatedTestLoader);
        updatedTestLoader.testsAmount(UPDATED_TESTS_AMOUNT);

        restTestLoaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestLoader.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestLoader))
            )
            .andExpect(status().isOk());

        // Validate the TestLoader in the database
        List<TestLoader> testLoaderList = testLoaderRepository.findAll();
        assertThat(testLoaderList).hasSize(databaseSizeBeforeUpdate);
        TestLoader testTestLoader = testLoaderList.get(testLoaderList.size() - 1);
        assertThat(testTestLoader.getTestsAmount()).isEqualTo(UPDATED_TESTS_AMOUNT);
    }

    @Test
    @Transactional
    void putNonExistingTestLoader() throws Exception {
        int databaseSizeBeforeUpdate = testLoaderRepository.findAll().size();
        testLoader.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestLoaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testLoader.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testLoader))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestLoader in the database
        List<TestLoader> testLoaderList = testLoaderRepository.findAll();
        assertThat(testLoaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestLoader() throws Exception {
        int databaseSizeBeforeUpdate = testLoaderRepository.findAll().size();
        testLoader.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestLoaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testLoader))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestLoader in the database
        List<TestLoader> testLoaderList = testLoaderRepository.findAll();
        assertThat(testLoaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestLoader() throws Exception {
        int databaseSizeBeforeUpdate = testLoaderRepository.findAll().size();
        testLoader.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestLoaderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testLoader)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestLoader in the database
        List<TestLoader> testLoaderList = testLoaderRepository.findAll();
        assertThat(testLoaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestLoaderWithPatch() throws Exception {
        // Initialize the database
        testLoaderRepository.saveAndFlush(testLoader);

        int databaseSizeBeforeUpdate = testLoaderRepository.findAll().size();

        // Update the testLoader using partial update
        TestLoader partialUpdatedTestLoader = new TestLoader();
        partialUpdatedTestLoader.setId(testLoader.getId());

        restTestLoaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestLoader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestLoader))
            )
            .andExpect(status().isOk());

        // Validate the TestLoader in the database
        List<TestLoader> testLoaderList = testLoaderRepository.findAll();
        assertThat(testLoaderList).hasSize(databaseSizeBeforeUpdate);
        TestLoader testTestLoader = testLoaderList.get(testLoaderList.size() - 1);
        assertThat(testTestLoader.getTestsAmount()).isEqualTo(DEFAULT_TESTS_AMOUNT);
    }

    @Test
    @Transactional
    void fullUpdateTestLoaderWithPatch() throws Exception {
        // Initialize the database
        testLoaderRepository.saveAndFlush(testLoader);

        int databaseSizeBeforeUpdate = testLoaderRepository.findAll().size();

        // Update the testLoader using partial update
        TestLoader partialUpdatedTestLoader = new TestLoader();
        partialUpdatedTestLoader.setId(testLoader.getId());

        partialUpdatedTestLoader.testsAmount(UPDATED_TESTS_AMOUNT);

        restTestLoaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestLoader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestLoader))
            )
            .andExpect(status().isOk());

        // Validate the TestLoader in the database
        List<TestLoader> testLoaderList = testLoaderRepository.findAll();
        assertThat(testLoaderList).hasSize(databaseSizeBeforeUpdate);
        TestLoader testTestLoader = testLoaderList.get(testLoaderList.size() - 1);
        assertThat(testTestLoader.getTestsAmount()).isEqualTo(UPDATED_TESTS_AMOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingTestLoader() throws Exception {
        int databaseSizeBeforeUpdate = testLoaderRepository.findAll().size();
        testLoader.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestLoaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testLoader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testLoader))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestLoader in the database
        List<TestLoader> testLoaderList = testLoaderRepository.findAll();
        assertThat(testLoaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestLoader() throws Exception {
        int databaseSizeBeforeUpdate = testLoaderRepository.findAll().size();
        testLoader.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestLoaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testLoader))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestLoader in the database
        List<TestLoader> testLoaderList = testLoaderRepository.findAll();
        assertThat(testLoaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestLoader() throws Exception {
        int databaseSizeBeforeUpdate = testLoaderRepository.findAll().size();
        testLoader.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestLoaderMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(testLoader))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestLoader in the database
        List<TestLoader> testLoaderList = testLoaderRepository.findAll();
        assertThat(testLoaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestLoader() throws Exception {
        // Initialize the database
        testLoaderRepository.saveAndFlush(testLoader);

        int databaseSizeBeforeDelete = testLoaderRepository.findAll().size();

        // Delete the testLoader
        restTestLoaderMockMvc
            .perform(delete(ENTITY_API_URL_ID, testLoader.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestLoader> testLoaderList = testLoaderRepository.findAll();
        assertThat(testLoaderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
