package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.TestUser;
import com.mycompany.myapp.repository.TestUserRepository;
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
 * Integration tests for the {@link TestUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestUserResourceIT {

    private static final Float DEFAULT_MARKS = 1F;
    private static final Float UPDATED_MARKS = 2F;

    private static final String ENTITY_API_URL = "/api/test-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestUserRepository testUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestUserMockMvc;

    private TestUser testUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestUser createEntity(EntityManager em) {
        TestUser testUser = new TestUser().marks(DEFAULT_MARKS);
        return testUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestUser createUpdatedEntity(EntityManager em) {
        TestUser testUser = new TestUser().marks(UPDATED_MARKS);
        return testUser;
    }

    @BeforeEach
    public void initTest() {
        testUser = createEntity(em);
    }

    @Test
    @Transactional
    void createTestUser() throws Exception {
        int databaseSizeBeforeCreate = testUserRepository.findAll().size();
        // Create the TestUser
        restTestUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testUser)))
            .andExpect(status().isCreated());

        // Validate the TestUser in the database
        List<TestUser> testUserList = testUserRepository.findAll();
        assertThat(testUserList).hasSize(databaseSizeBeforeCreate + 1);
        TestUser testTestUser = testUserList.get(testUserList.size() - 1);
        assertThat(testTestUser.getMarks()).isEqualTo(DEFAULT_MARKS);
    }

    @Test
    @Transactional
    void createTestUserWithExistingId() throws Exception {
        // Create the TestUser with an existing ID
        testUser.setId(1L);

        int databaseSizeBeforeCreate = testUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testUser)))
            .andExpect(status().isBadRequest());

        // Validate the TestUser in the database
        List<TestUser> testUserList = testUserRepository.findAll();
        assertThat(testUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestUsers() throws Exception {
        // Initialize the database
        testUserRepository.saveAndFlush(testUser);

        // Get all the testUserList
        restTestUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].marks").value(hasItem(DEFAULT_MARKS.doubleValue())));
    }

    @Test
    @Transactional
    void getTestUser() throws Exception {
        // Initialize the database
        testUserRepository.saveAndFlush(testUser);

        // Get the testUser
        restTestUserMockMvc
            .perform(get(ENTITY_API_URL_ID, testUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testUser.getId().intValue()))
            .andExpect(jsonPath("$.marks").value(DEFAULT_MARKS.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingTestUser() throws Exception {
        // Get the testUser
        restTestUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestUser() throws Exception {
        // Initialize the database
        testUserRepository.saveAndFlush(testUser);

        int databaseSizeBeforeUpdate = testUserRepository.findAll().size();

        // Update the testUser
        TestUser updatedTestUser = testUserRepository.findById(testUser.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTestUser are not directly saved in db
        em.detach(updatedTestUser);
        updatedTestUser.marks(UPDATED_MARKS);

        restTestUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestUser))
            )
            .andExpect(status().isOk());

        // Validate the TestUser in the database
        List<TestUser> testUserList = testUserRepository.findAll();
        assertThat(testUserList).hasSize(databaseSizeBeforeUpdate);
        TestUser testTestUser = testUserList.get(testUserList.size() - 1);
        assertThat(testTestUser.getMarks()).isEqualTo(UPDATED_MARKS);
    }

    @Test
    @Transactional
    void putNonExistingTestUser() throws Exception {
        int databaseSizeBeforeUpdate = testUserRepository.findAll().size();
        testUser.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestUser in the database
        List<TestUser> testUserList = testUserRepository.findAll();
        assertThat(testUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestUser() throws Exception {
        int databaseSizeBeforeUpdate = testUserRepository.findAll().size();
        testUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestUser in the database
        List<TestUser> testUserList = testUserRepository.findAll();
        assertThat(testUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestUser() throws Exception {
        int databaseSizeBeforeUpdate = testUserRepository.findAll().size();
        testUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestUser in the database
        List<TestUser> testUserList = testUserRepository.findAll();
        assertThat(testUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestUserWithPatch() throws Exception {
        // Initialize the database
        testUserRepository.saveAndFlush(testUser);

        int databaseSizeBeforeUpdate = testUserRepository.findAll().size();

        // Update the testUser using partial update
        TestUser partialUpdatedTestUser = new TestUser();
        partialUpdatedTestUser.setId(testUser.getId());

        partialUpdatedTestUser.marks(UPDATED_MARKS);

        restTestUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestUser))
            )
            .andExpect(status().isOk());

        // Validate the TestUser in the database
        List<TestUser> testUserList = testUserRepository.findAll();
        assertThat(testUserList).hasSize(databaseSizeBeforeUpdate);
        TestUser testTestUser = testUserList.get(testUserList.size() - 1);
        assertThat(testTestUser.getMarks()).isEqualTo(UPDATED_MARKS);
    }

    @Test
    @Transactional
    void fullUpdateTestUserWithPatch() throws Exception {
        // Initialize the database
        testUserRepository.saveAndFlush(testUser);

        int databaseSizeBeforeUpdate = testUserRepository.findAll().size();

        // Update the testUser using partial update
        TestUser partialUpdatedTestUser = new TestUser();
        partialUpdatedTestUser.setId(testUser.getId());

        partialUpdatedTestUser.marks(UPDATED_MARKS);

        restTestUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestUser))
            )
            .andExpect(status().isOk());

        // Validate the TestUser in the database
        List<TestUser> testUserList = testUserRepository.findAll();
        assertThat(testUserList).hasSize(databaseSizeBeforeUpdate);
        TestUser testTestUser = testUserList.get(testUserList.size() - 1);
        assertThat(testTestUser.getMarks()).isEqualTo(UPDATED_MARKS);
    }

    @Test
    @Transactional
    void patchNonExistingTestUser() throws Exception {
        int databaseSizeBeforeUpdate = testUserRepository.findAll().size();
        testUser.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestUser in the database
        List<TestUser> testUserList = testUserRepository.findAll();
        assertThat(testUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestUser() throws Exception {
        int databaseSizeBeforeUpdate = testUserRepository.findAll().size();
        testUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestUser in the database
        List<TestUser> testUserList = testUserRepository.findAll();
        assertThat(testUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestUser() throws Exception {
        int databaseSizeBeforeUpdate = testUserRepository.findAll().size();
        testUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(testUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestUser in the database
        List<TestUser> testUserList = testUserRepository.findAll();
        assertThat(testUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestUser() throws Exception {
        // Initialize the database
        testUserRepository.saveAndFlush(testUser);

        int databaseSizeBeforeDelete = testUserRepository.findAll().size();

        // Delete the testUser
        restTestUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, testUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestUser> testUserList = testUserRepository.findAll();
        assertThat(testUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
