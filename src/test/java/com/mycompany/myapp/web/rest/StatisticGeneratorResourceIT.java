package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.StatisticGenerator;
import com.mycompany.myapp.repository.StatisticGeneratorRepository;
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
 * Integration tests for the {@link StatisticGeneratorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StatisticGeneratorResourceIT {

    private static final Integer DEFAULT_GENERATED_REPORTS_AMOUNT = 1;
    private static final Integer UPDATED_GENERATED_REPORTS_AMOUNT = 2;

    private static final String ENTITY_API_URL = "/api/statistic-generators";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StatisticGeneratorRepository statisticGeneratorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStatisticGeneratorMockMvc;

    private StatisticGenerator statisticGenerator;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StatisticGenerator createEntity(EntityManager em) {
        StatisticGenerator statisticGenerator = new StatisticGenerator().generatedReportsAmount(DEFAULT_GENERATED_REPORTS_AMOUNT);
        return statisticGenerator;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StatisticGenerator createUpdatedEntity(EntityManager em) {
        StatisticGenerator statisticGenerator = new StatisticGenerator().generatedReportsAmount(UPDATED_GENERATED_REPORTS_AMOUNT);
        return statisticGenerator;
    }

    @BeforeEach
    public void initTest() {
        statisticGenerator = createEntity(em);
    }

    @Test
    @Transactional
    void createStatisticGenerator() throws Exception {
        int databaseSizeBeforeCreate = statisticGeneratorRepository.findAll().size();
        // Create the StatisticGenerator
        restStatisticGeneratorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(statisticGenerator))
            )
            .andExpect(status().isCreated());

        // Validate the StatisticGenerator in the database
        List<StatisticGenerator> statisticGeneratorList = statisticGeneratorRepository.findAll();
        assertThat(statisticGeneratorList).hasSize(databaseSizeBeforeCreate + 1);
        StatisticGenerator testStatisticGenerator = statisticGeneratorList.get(statisticGeneratorList.size() - 1);
        assertThat(testStatisticGenerator.getGeneratedReportsAmount()).isEqualTo(DEFAULT_GENERATED_REPORTS_AMOUNT);
    }

    @Test
    @Transactional
    void createStatisticGeneratorWithExistingId() throws Exception {
        // Create the StatisticGenerator with an existing ID
        statisticGenerator.setId(1L);

        int databaseSizeBeforeCreate = statisticGeneratorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStatisticGeneratorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(statisticGenerator))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatisticGenerator in the database
        List<StatisticGenerator> statisticGeneratorList = statisticGeneratorRepository.findAll();
        assertThat(statisticGeneratorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStatisticGenerators() throws Exception {
        // Initialize the database
        statisticGeneratorRepository.saveAndFlush(statisticGenerator);

        // Get all the statisticGeneratorList
        restStatisticGeneratorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(statisticGenerator.getId().intValue())))
            .andExpect(jsonPath("$.[*].generatedReportsAmount").value(hasItem(DEFAULT_GENERATED_REPORTS_AMOUNT)));
    }

    @Test
    @Transactional
    void getStatisticGenerator() throws Exception {
        // Initialize the database
        statisticGeneratorRepository.saveAndFlush(statisticGenerator);

        // Get the statisticGenerator
        restStatisticGeneratorMockMvc
            .perform(get(ENTITY_API_URL_ID, statisticGenerator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(statisticGenerator.getId().intValue()))
            .andExpect(jsonPath("$.generatedReportsAmount").value(DEFAULT_GENERATED_REPORTS_AMOUNT));
    }

    @Test
    @Transactional
    void getNonExistingStatisticGenerator() throws Exception {
        // Get the statisticGenerator
        restStatisticGeneratorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStatisticGenerator() throws Exception {
        // Initialize the database
        statisticGeneratorRepository.saveAndFlush(statisticGenerator);

        int databaseSizeBeforeUpdate = statisticGeneratorRepository.findAll().size();

        // Update the statisticGenerator
        StatisticGenerator updatedStatisticGenerator = statisticGeneratorRepository.findById(statisticGenerator.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStatisticGenerator are not directly saved in db
        em.detach(updatedStatisticGenerator);
        updatedStatisticGenerator.generatedReportsAmount(UPDATED_GENERATED_REPORTS_AMOUNT);

        restStatisticGeneratorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStatisticGenerator.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStatisticGenerator))
            )
            .andExpect(status().isOk());

        // Validate the StatisticGenerator in the database
        List<StatisticGenerator> statisticGeneratorList = statisticGeneratorRepository.findAll();
        assertThat(statisticGeneratorList).hasSize(databaseSizeBeforeUpdate);
        StatisticGenerator testStatisticGenerator = statisticGeneratorList.get(statisticGeneratorList.size() - 1);
        assertThat(testStatisticGenerator.getGeneratedReportsAmount()).isEqualTo(UPDATED_GENERATED_REPORTS_AMOUNT);
    }

    @Test
    @Transactional
    void putNonExistingStatisticGenerator() throws Exception {
        int databaseSizeBeforeUpdate = statisticGeneratorRepository.findAll().size();
        statisticGenerator.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatisticGeneratorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, statisticGenerator.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(statisticGenerator))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatisticGenerator in the database
        List<StatisticGenerator> statisticGeneratorList = statisticGeneratorRepository.findAll();
        assertThat(statisticGeneratorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStatisticGenerator() throws Exception {
        int databaseSizeBeforeUpdate = statisticGeneratorRepository.findAll().size();
        statisticGenerator.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatisticGeneratorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(statisticGenerator))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatisticGenerator in the database
        List<StatisticGenerator> statisticGeneratorList = statisticGeneratorRepository.findAll();
        assertThat(statisticGeneratorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStatisticGenerator() throws Exception {
        int databaseSizeBeforeUpdate = statisticGeneratorRepository.findAll().size();
        statisticGenerator.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatisticGeneratorMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(statisticGenerator))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StatisticGenerator in the database
        List<StatisticGenerator> statisticGeneratorList = statisticGeneratorRepository.findAll();
        assertThat(statisticGeneratorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStatisticGeneratorWithPatch() throws Exception {
        // Initialize the database
        statisticGeneratorRepository.saveAndFlush(statisticGenerator);

        int databaseSizeBeforeUpdate = statisticGeneratorRepository.findAll().size();

        // Update the statisticGenerator using partial update
        StatisticGenerator partialUpdatedStatisticGenerator = new StatisticGenerator();
        partialUpdatedStatisticGenerator.setId(statisticGenerator.getId());

        restStatisticGeneratorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStatisticGenerator.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStatisticGenerator))
            )
            .andExpect(status().isOk());

        // Validate the StatisticGenerator in the database
        List<StatisticGenerator> statisticGeneratorList = statisticGeneratorRepository.findAll();
        assertThat(statisticGeneratorList).hasSize(databaseSizeBeforeUpdate);
        StatisticGenerator testStatisticGenerator = statisticGeneratorList.get(statisticGeneratorList.size() - 1);
        assertThat(testStatisticGenerator.getGeneratedReportsAmount()).isEqualTo(DEFAULT_GENERATED_REPORTS_AMOUNT);
    }

    @Test
    @Transactional
    void fullUpdateStatisticGeneratorWithPatch() throws Exception {
        // Initialize the database
        statisticGeneratorRepository.saveAndFlush(statisticGenerator);

        int databaseSizeBeforeUpdate = statisticGeneratorRepository.findAll().size();

        // Update the statisticGenerator using partial update
        StatisticGenerator partialUpdatedStatisticGenerator = new StatisticGenerator();
        partialUpdatedStatisticGenerator.setId(statisticGenerator.getId());

        partialUpdatedStatisticGenerator.generatedReportsAmount(UPDATED_GENERATED_REPORTS_AMOUNT);

        restStatisticGeneratorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStatisticGenerator.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStatisticGenerator))
            )
            .andExpect(status().isOk());

        // Validate the StatisticGenerator in the database
        List<StatisticGenerator> statisticGeneratorList = statisticGeneratorRepository.findAll();
        assertThat(statisticGeneratorList).hasSize(databaseSizeBeforeUpdate);
        StatisticGenerator testStatisticGenerator = statisticGeneratorList.get(statisticGeneratorList.size() - 1);
        assertThat(testStatisticGenerator.getGeneratedReportsAmount()).isEqualTo(UPDATED_GENERATED_REPORTS_AMOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingStatisticGenerator() throws Exception {
        int databaseSizeBeforeUpdate = statisticGeneratorRepository.findAll().size();
        statisticGenerator.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatisticGeneratorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, statisticGenerator.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(statisticGenerator))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatisticGenerator in the database
        List<StatisticGenerator> statisticGeneratorList = statisticGeneratorRepository.findAll();
        assertThat(statisticGeneratorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStatisticGenerator() throws Exception {
        int databaseSizeBeforeUpdate = statisticGeneratorRepository.findAll().size();
        statisticGenerator.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatisticGeneratorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(statisticGenerator))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatisticGenerator in the database
        List<StatisticGenerator> statisticGeneratorList = statisticGeneratorRepository.findAll();
        assertThat(statisticGeneratorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStatisticGenerator() throws Exception {
        int databaseSizeBeforeUpdate = statisticGeneratorRepository.findAll().size();
        statisticGenerator.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatisticGeneratorMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(statisticGenerator))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StatisticGenerator in the database
        List<StatisticGenerator> statisticGeneratorList = statisticGeneratorRepository.findAll();
        assertThat(statisticGeneratorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStatisticGenerator() throws Exception {
        // Initialize the database
        statisticGeneratorRepository.saveAndFlush(statisticGenerator);

        int databaseSizeBeforeDelete = statisticGeneratorRepository.findAll().size();

        // Delete the statisticGenerator
        restStatisticGeneratorMockMvc
            .perform(delete(ENTITY_API_URL_ID, statisticGenerator.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StatisticGenerator> statisticGeneratorList = statisticGeneratorRepository.findAll();
        assertThat(statisticGeneratorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
