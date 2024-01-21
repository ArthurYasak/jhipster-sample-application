package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ThemeFile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.ThemeFile}.
 */
public interface ThemeFileService {
    /**
     * Save a themeFile.
     *
     * @param themeFile the entity to save.
     * @return the persisted entity.
     */
    ThemeFile save(ThemeFile themeFile);

    /**
     * Updates a themeFile.
     *
     * @param themeFile the entity to update.
     * @return the persisted entity.
     */
    ThemeFile update(ThemeFile themeFile);

    /**
     * Partially updates a themeFile.
     *
     * @param themeFile the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ThemeFile> partialUpdate(ThemeFile themeFile);

    /**
     * Get all the themeFiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ThemeFile> findAll(Pageable pageable);

    /**
     * Get all the ThemeFile where TestEntity is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<ThemeFile> findAllWhereTestEntityIsNull();

    /**
     * Get the "id" themeFile.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ThemeFile> findOne(Long id);

    /**
     * Delete the "id" themeFile.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
