package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.TestEntity;
import com.mycompany.myapp.repository.TestEntityRepository;
import com.mycompany.myapp.service.TestEntityService;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TestEntityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TestEntityResourceIT {

    private static final String DEFAULT_TEST_POINTS = "AAAAAAAAAA";
    private static final String UPDATED_TEST_POINTS = "BBBBBBBBBB";

    private static final String DEFAULT_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_RESULT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/test-entities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestEntityRepository testEntityRepository;

    @Mock
    private TestEntityRepository testEntityRepositoryMock;

    @Mock
    private TestEntityService testEntityServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestEntityMockMvc;

    private TestEntity testEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestEntity createEntity(EntityManager em) {
        TestEntity testEntity = new TestEntity().testPoints(DEFAULT_TEST_POINTS).result(DEFAULT_RESULT);
        return testEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestEntity createUpdatedEntity(EntityManager em) {
        TestEntity testEntity = new TestEntity().testPoints(UPDATED_TEST_POINTS).result(UPDATED_RESULT);
        return testEntity;
    }

    @BeforeEach
    public void initTest() {
        testEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createTestEntity() throws Exception {
        int databaseSizeBeforeCreate = testEntityRepository.findAll().size();
        // Create the TestEntity
        restTestEntityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testEntity)))
            .andExpect(status().isCreated());

        // Validate the TestEntity in the database
        List<TestEntity> testEntityList = testEntityRepository.findAll();
        assertThat(testEntityList).hasSize(databaseSizeBeforeCreate + 1);
        TestEntity testTestEntity = testEntityList.get(testEntityList.size() - 1);
        assertThat(testTestEntity.getTestPoints()).isEqualTo(DEFAULT_TEST_POINTS);
        assertThat(testTestEntity.getResult()).isEqualTo(DEFAULT_RESULT);
    }

    @Test
    @Transactional
    void createTestEntityWithExistingId() throws Exception {
        // Create the TestEntity with an existing ID
        testEntity.setId(1L);

        int databaseSizeBeforeCreate = testEntityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestEntityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testEntity)))
            .andExpect(status().isBadRequest());

        // Validate the TestEntity in the database
        List<TestEntity> testEntityList = testEntityRepository.findAll();
        assertThat(testEntityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestEntities() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList
        restTestEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].testPoints").value(hasItem(DEFAULT_TEST_POINTS)))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTestEntitiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(testEntityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTestEntityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(testEntityServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTestEntitiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(testEntityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTestEntityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(testEntityRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTestEntity() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get the testEntity
        restTestEntityMockMvc
            .perform(get(ENTITY_API_URL_ID, testEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testEntity.getId().intValue()))
            .andExpect(jsonPath("$.testPoints").value(DEFAULT_TEST_POINTS))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT));
    }

    @Test
    @Transactional
    void getNonExistingTestEntity() throws Exception {
        // Get the testEntity
        restTestEntityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestEntity() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        int databaseSizeBeforeUpdate = testEntityRepository.findAll().size();

        // Update the testEntity
        TestEntity updatedTestEntity = testEntityRepository.findById(testEntity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTestEntity are not directly saved in db
        em.detach(updatedTestEntity);
        updatedTestEntity.testPoints(UPDATED_TEST_POINTS).result(UPDATED_RESULT);

        restTestEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestEntity))
            )
            .andExpect(status().isOk());

        // Validate the TestEntity in the database
        List<TestEntity> testEntityList = testEntityRepository.findAll();
        assertThat(testEntityList).hasSize(databaseSizeBeforeUpdate);
        TestEntity testTestEntity = testEntityList.get(testEntityList.size() - 1);
        assertThat(testTestEntity.getTestPoints()).isEqualTo(UPDATED_TEST_POINTS);
        assertThat(testTestEntity.getResult()).isEqualTo(UPDATED_RESULT);
    }

    @Test
    @Transactional
    void putNonExistingTestEntity() throws Exception {
        int databaseSizeBeforeUpdate = testEntityRepository.findAll().size();
        testEntity.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestEntity in the database
        List<TestEntity> testEntityList = testEntityRepository.findAll();
        assertThat(testEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestEntity() throws Exception {
        int databaseSizeBeforeUpdate = testEntityRepository.findAll().size();
        testEntity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestEntity in the database
        List<TestEntity> testEntityList = testEntityRepository.findAll();
        assertThat(testEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestEntity() throws Exception {
        int databaseSizeBeforeUpdate = testEntityRepository.findAll().size();
        testEntity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestEntityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testEntity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestEntity in the database
        List<TestEntity> testEntityList = testEntityRepository.findAll();
        assertThat(testEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestEntityWithPatch() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        int databaseSizeBeforeUpdate = testEntityRepository.findAll().size();

        // Update the testEntity using partial update
        TestEntity partialUpdatedTestEntity = new TestEntity();
        partialUpdatedTestEntity.setId(testEntity.getId());

        partialUpdatedTestEntity.result(UPDATED_RESULT);

        restTestEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestEntity))
            )
            .andExpect(status().isOk());

        // Validate the TestEntity in the database
        List<TestEntity> testEntityList = testEntityRepository.findAll();
        assertThat(testEntityList).hasSize(databaseSizeBeforeUpdate);
        TestEntity testTestEntity = testEntityList.get(testEntityList.size() - 1);
        assertThat(testTestEntity.getTestPoints()).isEqualTo(DEFAULT_TEST_POINTS);
        assertThat(testTestEntity.getResult()).isEqualTo(UPDATED_RESULT);
    }

    @Test
    @Transactional
    void fullUpdateTestEntityWithPatch() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        int databaseSizeBeforeUpdate = testEntityRepository.findAll().size();

        // Update the testEntity using partial update
        TestEntity partialUpdatedTestEntity = new TestEntity();
        partialUpdatedTestEntity.setId(testEntity.getId());

        partialUpdatedTestEntity.testPoints(UPDATED_TEST_POINTS).result(UPDATED_RESULT);

        restTestEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestEntity))
            )
            .andExpect(status().isOk());

        // Validate the TestEntity in the database
        List<TestEntity> testEntityList = testEntityRepository.findAll();
        assertThat(testEntityList).hasSize(databaseSizeBeforeUpdate);
        TestEntity testTestEntity = testEntityList.get(testEntityList.size() - 1);
        assertThat(testTestEntity.getTestPoints()).isEqualTo(UPDATED_TEST_POINTS);
        assertThat(testTestEntity.getResult()).isEqualTo(UPDATED_RESULT);
    }

    @Test
    @Transactional
    void patchNonExistingTestEntity() throws Exception {
        int databaseSizeBeforeUpdate = testEntityRepository.findAll().size();
        testEntity.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestEntity in the database
        List<TestEntity> testEntityList = testEntityRepository.findAll();
        assertThat(testEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestEntity() throws Exception {
        int databaseSizeBeforeUpdate = testEntityRepository.findAll().size();
        testEntity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestEntity in the database
        List<TestEntity> testEntityList = testEntityRepository.findAll();
        assertThat(testEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestEntity() throws Exception {
        int databaseSizeBeforeUpdate = testEntityRepository.findAll().size();
        testEntity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestEntityMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(testEntity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestEntity in the database
        List<TestEntity> testEntityList = testEntityRepository.findAll();
        assertThat(testEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestEntity() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        int databaseSizeBeforeDelete = testEntityRepository.findAll().size();

        // Delete the testEntity
        restTestEntityMockMvc
            .perform(delete(ENTITY_API_URL_ID, testEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestEntity> testEntityList = testEntityRepository.findAll();
        assertThat(testEntityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
