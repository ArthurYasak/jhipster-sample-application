package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Tester;
import com.mycompany.myapp.repository.TesterRepository;
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
 * Integration tests for the {@link TesterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TesterResourceIT {

    private static final Integer DEFAULT_HOLD_TESTS = 1;
    private static final Integer UPDATED_HOLD_TESTS = 2;

    private static final String ENTITY_API_URL = "/api/testers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TesterRepository testerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTesterMockMvc;

    private Tester tester;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tester createEntity(EntityManager em) {
        Tester tester = new Tester().holdTests(DEFAULT_HOLD_TESTS);
        return tester;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tester createUpdatedEntity(EntityManager em) {
        Tester tester = new Tester().holdTests(UPDATED_HOLD_TESTS);
        return tester;
    }

    @BeforeEach
    public void initTest() {
        tester = createEntity(em);
    }

    @Test
    @Transactional
    void createTester() throws Exception {
        int databaseSizeBeforeCreate = testerRepository.findAll().size();
        // Create the Tester
        restTesterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tester)))
            .andExpect(status().isCreated());

        // Validate the Tester in the database
        List<Tester> testerList = testerRepository.findAll();
        assertThat(testerList).hasSize(databaseSizeBeforeCreate + 1);
        Tester testTester = testerList.get(testerList.size() - 1);
        assertThat(testTester.getHoldTests()).isEqualTo(DEFAULT_HOLD_TESTS);
    }

    @Test
    @Transactional
    void createTesterWithExistingId() throws Exception {
        // Create the Tester with an existing ID
        tester.setId(1L);

        int databaseSizeBeforeCreate = testerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTesterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tester)))
            .andExpect(status().isBadRequest());

        // Validate the Tester in the database
        List<Tester> testerList = testerRepository.findAll();
        assertThat(testerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTesters() throws Exception {
        // Initialize the database
        testerRepository.saveAndFlush(tester);

        // Get all the testerList
        restTesterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tester.getId().intValue())))
            .andExpect(jsonPath("$.[*].holdTests").value(hasItem(DEFAULT_HOLD_TESTS)));
    }

    @Test
    @Transactional
    void getTester() throws Exception {
        // Initialize the database
        testerRepository.saveAndFlush(tester);

        // Get the tester
        restTesterMockMvc
            .perform(get(ENTITY_API_URL_ID, tester.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tester.getId().intValue()))
            .andExpect(jsonPath("$.holdTests").value(DEFAULT_HOLD_TESTS));
    }

    @Test
    @Transactional
    void getNonExistingTester() throws Exception {
        // Get the tester
        restTesterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTester() throws Exception {
        // Initialize the database
        testerRepository.saveAndFlush(tester);

        int databaseSizeBeforeUpdate = testerRepository.findAll().size();

        // Update the tester
        Tester updatedTester = testerRepository.findById(tester.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTester are not directly saved in db
        em.detach(updatedTester);
        updatedTester.holdTests(UPDATED_HOLD_TESTS);

        restTesterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTester.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTester))
            )
            .andExpect(status().isOk());

        // Validate the Tester in the database
        List<Tester> testerList = testerRepository.findAll();
        assertThat(testerList).hasSize(databaseSizeBeforeUpdate);
        Tester testTester = testerList.get(testerList.size() - 1);
        assertThat(testTester.getHoldTests()).isEqualTo(UPDATED_HOLD_TESTS);
    }

    @Test
    @Transactional
    void putNonExistingTester() throws Exception {
        int databaseSizeBeforeUpdate = testerRepository.findAll().size();
        tester.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTesterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tester.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tester))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tester in the database
        List<Tester> testerList = testerRepository.findAll();
        assertThat(testerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTester() throws Exception {
        int databaseSizeBeforeUpdate = testerRepository.findAll().size();
        tester.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTesterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tester))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tester in the database
        List<Tester> testerList = testerRepository.findAll();
        assertThat(testerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTester() throws Exception {
        int databaseSizeBeforeUpdate = testerRepository.findAll().size();
        tester.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTesterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tester)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tester in the database
        List<Tester> testerList = testerRepository.findAll();
        assertThat(testerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTesterWithPatch() throws Exception {
        // Initialize the database
        testerRepository.saveAndFlush(tester);

        int databaseSizeBeforeUpdate = testerRepository.findAll().size();

        // Update the tester using partial update
        Tester partialUpdatedTester = new Tester();
        partialUpdatedTester.setId(tester.getId());

        partialUpdatedTester.holdTests(UPDATED_HOLD_TESTS);

        restTesterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTester.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTester))
            )
            .andExpect(status().isOk());

        // Validate the Tester in the database
        List<Tester> testerList = testerRepository.findAll();
        assertThat(testerList).hasSize(databaseSizeBeforeUpdate);
        Tester testTester = testerList.get(testerList.size() - 1);
        assertThat(testTester.getHoldTests()).isEqualTo(UPDATED_HOLD_TESTS);
    }

    @Test
    @Transactional
    void fullUpdateTesterWithPatch() throws Exception {
        // Initialize the database
        testerRepository.saveAndFlush(tester);

        int databaseSizeBeforeUpdate = testerRepository.findAll().size();

        // Update the tester using partial update
        Tester partialUpdatedTester = new Tester();
        partialUpdatedTester.setId(tester.getId());

        partialUpdatedTester.holdTests(UPDATED_HOLD_TESTS);

        restTesterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTester.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTester))
            )
            .andExpect(status().isOk());

        // Validate the Tester in the database
        List<Tester> testerList = testerRepository.findAll();
        assertThat(testerList).hasSize(databaseSizeBeforeUpdate);
        Tester testTester = testerList.get(testerList.size() - 1);
        assertThat(testTester.getHoldTests()).isEqualTo(UPDATED_HOLD_TESTS);
    }

    @Test
    @Transactional
    void patchNonExistingTester() throws Exception {
        int databaseSizeBeforeUpdate = testerRepository.findAll().size();
        tester.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTesterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tester.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tester))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tester in the database
        List<Tester> testerList = testerRepository.findAll();
        assertThat(testerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTester() throws Exception {
        int databaseSizeBeforeUpdate = testerRepository.findAll().size();
        tester.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTesterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tester))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tester in the database
        List<Tester> testerList = testerRepository.findAll();
        assertThat(testerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTester() throws Exception {
        int databaseSizeBeforeUpdate = testerRepository.findAll().size();
        tester.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTesterMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tester)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tester in the database
        List<Tester> testerList = testerRepository.findAll();
        assertThat(testerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTester() throws Exception {
        // Initialize the database
        testerRepository.saveAndFlush(tester);

        int databaseSizeBeforeDelete = testerRepository.findAll().size();

        // Delete the tester
        restTesterMockMvc
            .perform(delete(ENTITY_API_URL_ID, tester.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tester> testerList = testerRepository.findAll();
        assertThat(testerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
