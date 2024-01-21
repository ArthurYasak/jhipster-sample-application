package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.TestEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.TestEntity}.
 */
public interface TestEntityService {
    /**
     * Save a testEntity.
     *
     * @param testEntity the entity to save.
     * @return the persisted entity.
     */
    TestEntity save(TestEntity testEntity);

    /**
     * Updates a testEntity.
     *
     * @param testEntity the entity to update.
     * @return the persisted entity.
     */
    TestEntity update(TestEntity testEntity);

    /**
     * Partially updates a testEntity.
     *
     * @param testEntity the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TestEntity> partialUpdate(TestEntity testEntity);

    /**
     * Get all the testEntities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TestEntity> findAll(Pageable pageable);

    /**
     * Get all the testEntities with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TestEntity> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" testEntity.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TestEntity> findOne(Long id);

    /**
     * Delete the "id" testEntity.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
