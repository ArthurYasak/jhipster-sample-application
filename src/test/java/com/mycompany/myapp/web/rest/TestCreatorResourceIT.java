package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.TestCreator;
import com.mycompany.myapp.repository.TestCreatorRepository;
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
 * Integration tests for the {@link TestCreatorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestCreatorResourceIT {

    private static final Integer DEFAULT_TESTS_AMOUNT = 1;
    private static final Integer UPDATED_TESTS_AMOUNT = 2;

    private static final String ENTITY_API_URL = "/api/test-creators";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestCreatorRepository testCreatorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestCreatorMockMvc;

    private TestCreator testCreator;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCreator createEntity(EntityManager em) {
        TestCreator testCreator = new TestCreator().testsAmount(DEFAULT_TESTS_AMOUNT);
        return testCreator;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCreator createUpdatedEntity(EntityManager em) {
        TestCreator testCreator = new TestCreator().testsAmount(UPDATED_TESTS_AMOUNT);
        return testCreator;
    }

    @BeforeEach
    public void initTest() {
        testCreator = createEntity(em);
    }

    @Test
    @Transactional
    void createTestCreator() throws Exception {
        int databaseSizeBeforeCreate = testCreatorRepository.findAll().size();
        // Create the TestCreator
        restTestCreatorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCreator)))
            .andExpect(status().isCreated());

        // Validate the TestCreator in the database
        List<TestCreator> testCreatorList = testCreatorRepository.findAll();
        assertThat(testCreatorList).hasSize(databaseSizeBeforeCreate + 1);
        TestCreator testTestCreator = testCreatorList.get(testCreatorList.size() - 1);
        assertThat(testTestCreator.getTestsAmount()).isEqualTo(DEFAULT_TESTS_AMOUNT);
    }

    @Test
    @Transactional
    void createTestCreatorWithExistingId() throws Exception {
        // Create the TestCreator with an existing ID
        testCreator.setId(1L);

        int databaseSizeBeforeCreate = testCreatorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestCreatorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCreator)))
            .andExpect(status().isBadRequest());

        // Validate the TestCreator in the database
        List<TestCreator> testCreatorList = testCreatorRepository.findAll();
        assertThat(testCreatorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestCreators() throws Exception {
        // Initialize the database
        testCreatorRepository.saveAndFlush(testCreator);

        // Get all the testCreatorList
        restTestCreatorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCreator.getId().intValue())))
            .andExpect(jsonPath("$.[*].testsAmount").value(hasItem(DEFAULT_TESTS_AMOUNT)));
    }

    @Test
    @Transactional
    void getTestCreator() throws Exception {
        // Initialize the database
        testCreatorRepository.saveAndFlush(testCreator);

        // Get the testCreator
        restTestCreatorMockMvc
            .perform(get(ENTITY_API_URL_ID, testCreator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testCreator.getId().intValue()))
            .andExpect(jsonPath("$.testsAmount").value(DEFAULT_TESTS_AMOUNT));
    }

    @Test
    @Transactional
    void getNonExistingTestCreator() throws Exception {
        // Get the testCreator
        restTestCreatorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestCreator() throws Exception {
        // Initialize the database
        testCreatorRepository.saveAndFlush(testCreator);

        int databaseSizeBeforeUpdate = testCreatorRepository.findAll().size();

        // Update the testCreator
        TestCreator updatedTestCreator = testCreatorRepository.findById(testCreator.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTestCreator are not directly saved in db
        em.detach(updatedTestCreator);
        updatedTestCreator.testsAmount(UPDATED_TESTS_AMOUNT);

        restTestCreatorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestCreator.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestCreator))
            )
            .andExpect(status().isOk());

        // Validate the TestCreator in the database
        List<TestCreator> testCreatorList = testCreatorRepository.findAll();
        assertThat(testCreatorList).hasSize(databaseSizeBeforeUpdate);
        TestCreator testTestCreator = testCreatorList.get(testCreatorList.size() - 1);
        assertThat(testTestCreator.getTestsAmount()).isEqualTo(UPDATED_TESTS_AMOUNT);
    }

    @Test
    @Transactional
    void putNonExistingTestCreator() throws Exception {
        int databaseSizeBeforeUpdate = testCreatorRepository.findAll().size();
        testCreator.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCreatorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testCreator.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCreator))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCreator in the database
        List<TestCreator> testCreatorList = testCreatorRepository.findAll();
        assertThat(testCreatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestCreator() throws Exception {
        int databaseSizeBeforeUpdate = testCreatorRepository.findAll().size();
        testCreator.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCreatorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCreator))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCreator in the database
        List<TestCreator> testCreatorList = testCreatorRepository.findAll();
        assertThat(testCreatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestCreator() throws Exception {
        int databaseSizeBeforeUpdate = testCreatorRepository.findAll().size();
        testCreator.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCreatorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCreator)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCreator in the database
        List<TestCreator> testCreatorList = testCreatorRepository.findAll();
        assertThat(testCreatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestCreatorWithPatch() throws Exception {
        // Initialize the database
        testCreatorRepository.saveAndFlush(testCreator);

        int databaseSizeBeforeUpdate = testCreatorRepository.findAll().size();

        // Update the testCreator using partial update
        TestCreator partialUpdatedTestCreator = new TestCreator();
        partialUpdatedTestCreator.setId(testCreator.getId());

        restTestCreatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCreator.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestCreator))
            )
            .andExpect(status().isOk());

        // Validate the TestCreator in the database
        List<TestCreator> testCreatorList = testCreatorRepository.findAll();
        assertThat(testCreatorList).hasSize(databaseSizeBeforeUpdate);
        TestCreator testTestCreator = testCreatorList.get(testCreatorList.size() - 1);
        assertThat(testTestCreator.getTestsAmount()).isEqualTo(DEFAULT_TESTS_AMOUNT);
    }

    @Test
    @Transactional
    void fullUpdateTestCreatorWithPatch() throws Exception {
        // Initialize the database
        testCreatorRepository.saveAndFlush(testCreator);

        int databaseSizeBeforeUpdate = testCreatorRepository.findAll().size();

        // Update the testCreator using partial update
        TestCreator partialUpdatedTestCreator = new TestCreator();
        partialUpdatedTestCreator.setId(testCreator.getId());

        partialUpdatedTestCreator.testsAmount(UPDATED_TESTS_AMOUNT);

        restTestCreatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCreator.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestCreator))
            )
            .andExpect(status().isOk());

        // Validate the TestCreator in the database
        List<TestCreator> testCreatorList = testCreatorRepository.findAll();
        assertThat(testCreatorList).hasSize(databaseSizeBeforeUpdate);
        TestCreator testTestCreator = testCreatorList.get(testCreatorList.size() - 1);
        assertThat(testTestCreator.getTestsAmount()).isEqualTo(UPDATED_TESTS_AMOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingTestCreator() throws Exception {
        int databaseSizeBeforeUpdate = testCreatorRepository.findAll().size();
        testCreator.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCreatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testCreator.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCreator))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCreator in the database
        List<TestCreator> testCreatorList = testCreatorRepository.findAll();
        assertThat(testCreatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestCreator() throws Exception {
        int databaseSizeBeforeUpdate = testCreatorRepository.findAll().size();
        testCreator.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCreatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCreator))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCreator in the database
        List<TestCreator> testCreatorList = testCreatorRepository.findAll();
        assertThat(testCreatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestCreator() throws Exception {
        int databaseSizeBeforeUpdate = testCreatorRepository.findAll().size();
        testCreator.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCreatorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(testCreator))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCreator in the database
        List<TestCreator> testCreatorList = testCreatorRepository.findAll();
        assertThat(testCreatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestCreator() throws Exception {
        // Initialize the database
        testCreatorRepository.saveAndFlush(testCreator);

        int databaseSizeBeforeDelete = testCreatorRepository.findAll().size();

        // Delete the testCreator
        restTestCreatorMockMvc
            .perform(delete(ENTITY_API_URL_ID, testCreator.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestCreator> testCreatorList = testCreatorRepository.findAll();
        assertThat(testCreatorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
