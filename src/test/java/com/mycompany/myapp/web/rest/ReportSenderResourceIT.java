package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ReportSender;
import com.mycompany.myapp.repository.ReportSenderRepository;
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
 * Integration tests for the {@link ReportSenderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReportSenderResourceIT {

    private static final String DEFAULT_EMAIL_LIST = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_LIST = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/report-senders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReportSenderRepository reportSenderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportSenderMockMvc;

    private ReportSender reportSender;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportSender createEntity(EntityManager em) {
        ReportSender reportSender = new ReportSender().emailList(DEFAULT_EMAIL_LIST);
        return reportSender;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportSender createUpdatedEntity(EntityManager em) {
        ReportSender reportSender = new ReportSender().emailList(UPDATED_EMAIL_LIST);
        return reportSender;
    }

    @BeforeEach
    public void initTest() {
        reportSender = createEntity(em);
    }

    @Test
    @Transactional
    void createReportSender() throws Exception {
        int databaseSizeBeforeCreate = reportSenderRepository.findAll().size();
        // Create the ReportSender
        restReportSenderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportSender)))
            .andExpect(status().isCreated());

        // Validate the ReportSender in the database
        List<ReportSender> reportSenderList = reportSenderRepository.findAll();
        assertThat(reportSenderList).hasSize(databaseSizeBeforeCreate + 1);
        ReportSender testReportSender = reportSenderList.get(reportSenderList.size() - 1);
        assertThat(testReportSender.getEmailList()).isEqualTo(DEFAULT_EMAIL_LIST);
    }

    @Test
    @Transactional
    void createReportSenderWithExistingId() throws Exception {
        // Create the ReportSender with an existing ID
        reportSender.setId(1L);

        int databaseSizeBeforeCreate = reportSenderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportSenderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportSender)))
            .andExpect(status().isBadRequest());

        // Validate the ReportSender in the database
        List<ReportSender> reportSenderList = reportSenderRepository.findAll();
        assertThat(reportSenderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReportSenders() throws Exception {
        // Initialize the database
        reportSenderRepository.saveAndFlush(reportSender);

        // Get all the reportSenderList
        restReportSenderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportSender.getId().intValue())))
            .andExpect(jsonPath("$.[*].emailList").value(hasItem(DEFAULT_EMAIL_LIST)));
    }

    @Test
    @Transactional
    void getReportSender() throws Exception {
        // Initialize the database
        reportSenderRepository.saveAndFlush(reportSender);

        // Get the reportSender
        restReportSenderMockMvc
            .perform(get(ENTITY_API_URL_ID, reportSender.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportSender.getId().intValue()))
            .andExpect(jsonPath("$.emailList").value(DEFAULT_EMAIL_LIST));
    }

    @Test
    @Transactional
    void getNonExistingReportSender() throws Exception {
        // Get the reportSender
        restReportSenderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReportSender() throws Exception {
        // Initialize the database
        reportSenderRepository.saveAndFlush(reportSender);

        int databaseSizeBeforeUpdate = reportSenderRepository.findAll().size();

        // Update the reportSender
        ReportSender updatedReportSender = reportSenderRepository.findById(reportSender.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReportSender are not directly saved in db
        em.detach(updatedReportSender);
        updatedReportSender.emailList(UPDATED_EMAIL_LIST);

        restReportSenderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReportSender.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReportSender))
            )
            .andExpect(status().isOk());

        // Validate the ReportSender in the database
        List<ReportSender> reportSenderList = reportSenderRepository.findAll();
        assertThat(reportSenderList).hasSize(databaseSizeBeforeUpdate);
        ReportSender testReportSender = reportSenderList.get(reportSenderList.size() - 1);
        assertThat(testReportSender.getEmailList()).isEqualTo(UPDATED_EMAIL_LIST);
    }

    @Test
    @Transactional
    void putNonExistingReportSender() throws Exception {
        int databaseSizeBeforeUpdate = reportSenderRepository.findAll().size();
        reportSender.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportSenderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportSender.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportSender))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportSender in the database
        List<ReportSender> reportSenderList = reportSenderRepository.findAll();
        assertThat(reportSenderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportSender() throws Exception {
        int databaseSizeBeforeUpdate = reportSenderRepository.findAll().size();
        reportSender.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportSenderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportSender))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportSender in the database
        List<ReportSender> reportSenderList = reportSenderRepository.findAll();
        assertThat(reportSenderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportSender() throws Exception {
        int databaseSizeBeforeUpdate = reportSenderRepository.findAll().size();
        reportSender.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportSenderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportSender)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportSender in the database
        List<ReportSender> reportSenderList = reportSenderRepository.findAll();
        assertThat(reportSenderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReportSenderWithPatch() throws Exception {
        // Initialize the database
        reportSenderRepository.saveAndFlush(reportSender);

        int databaseSizeBeforeUpdate = reportSenderRepository.findAll().size();

        // Update the reportSender using partial update
        ReportSender partialUpdatedReportSender = new ReportSender();
        partialUpdatedReportSender.setId(reportSender.getId());

        restReportSenderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportSender.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReportSender))
            )
            .andExpect(status().isOk());

        // Validate the ReportSender in the database
        List<ReportSender> reportSenderList = reportSenderRepository.findAll();
        assertThat(reportSenderList).hasSize(databaseSizeBeforeUpdate);
        ReportSender testReportSender = reportSenderList.get(reportSenderList.size() - 1);
        assertThat(testReportSender.getEmailList()).isEqualTo(DEFAULT_EMAIL_LIST);
    }

    @Test
    @Transactional
    void fullUpdateReportSenderWithPatch() throws Exception {
        // Initialize the database
        reportSenderRepository.saveAndFlush(reportSender);

        int databaseSizeBeforeUpdate = reportSenderRepository.findAll().size();

        // Update the reportSender using partial update
        ReportSender partialUpdatedReportSender = new ReportSender();
        partialUpdatedReportSender.setId(reportSender.getId());

        partialUpdatedReportSender.emailList(UPDATED_EMAIL_LIST);

        restReportSenderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportSender.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReportSender))
            )
            .andExpect(status().isOk());

        // Validate the ReportSender in the database
        List<ReportSender> reportSenderList = reportSenderRepository.findAll();
        assertThat(reportSenderList).hasSize(databaseSizeBeforeUpdate);
        ReportSender testReportSender = reportSenderList.get(reportSenderList.size() - 1);
        assertThat(testReportSender.getEmailList()).isEqualTo(UPDATED_EMAIL_LIST);
    }

    @Test
    @Transactional
    void patchNonExistingReportSender() throws Exception {
        int databaseSizeBeforeUpdate = reportSenderRepository.findAll().size();
        reportSender.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportSenderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportSender.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportSender))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportSender in the database
        List<ReportSender> reportSenderList = reportSenderRepository.findAll();
        assertThat(reportSenderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportSender() throws Exception {
        int databaseSizeBeforeUpdate = reportSenderRepository.findAll().size();
        reportSender.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportSenderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportSender))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportSender in the database
        List<ReportSender> reportSenderList = reportSenderRepository.findAll();
        assertThat(reportSenderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportSender() throws Exception {
        int databaseSizeBeforeUpdate = reportSenderRepository.findAll().size();
        reportSender.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportSenderMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reportSender))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportSender in the database
        List<ReportSender> reportSenderList = reportSenderRepository.findAll();
        assertThat(reportSenderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReportSender() throws Exception {
        // Initialize the database
        reportSenderRepository.saveAndFlush(reportSender);

        int databaseSizeBeforeDelete = reportSenderRepository.findAll().size();

        // Delete the reportSender
        restReportSenderMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportSender.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReportSender> reportSenderList = reportSenderRepository.findAll();
        assertThat(reportSenderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
