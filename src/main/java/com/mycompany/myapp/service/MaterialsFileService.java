package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.MaterialsFile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.MaterialsFile}.
 */
public interface MaterialsFileService {
    /**
     * Save a materialsFile.
     *
     * @param materialsFile the entity to save.
     * @return the persisted entity.
     */
    MaterialsFile save(MaterialsFile materialsFile);

    /**
     * Updates a materialsFile.
     *
     * @param materialsFile the entity to update.
     * @return the persisted entity.
     */
    MaterialsFile update(MaterialsFile materialsFile);

    /**
     * Partially updates a materialsFile.
     *
     * @param materialsFile the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MaterialsFile> partialUpdate(MaterialsFile materialsFile);

    /**
     * Get all the materialsFiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MaterialsFile> findAll(Pageable pageable);

    /**
     * Get all the MaterialsFile where ThemeFile is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<MaterialsFile> findAllWhereThemeFileIsNull();

    /**
     * Get the "id" materialsFile.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MaterialsFile> findOne(Long id);

    /**
     * Delete the "id" materialsFile.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
