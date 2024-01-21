package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.MaterialsFile;
import com.mycompany.myapp.repository.MaterialsFileRepository;
import com.mycompany.myapp.service.MaterialsFileService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.MaterialsFile}.
 */
@Service
@Transactional
public class MaterialsFileServiceImpl implements MaterialsFileService {

    private final Logger log = LoggerFactory.getLogger(MaterialsFileServiceImpl.class);

    private final MaterialsFileRepository materialsFileRepository;

    public MaterialsFileServiceImpl(MaterialsFileRepository materialsFileRepository) {
        this.materialsFileRepository = materialsFileRepository;
    }

    @Override
    public MaterialsFile save(MaterialsFile materialsFile) {
        log.debug("Request to save MaterialsFile : {}", materialsFile);
        return materialsFileRepository.save(materialsFile);
    }

    @Override
    public MaterialsFile update(MaterialsFile materialsFile) {
        log.debug("Request to update MaterialsFile : {}", materialsFile);
        return materialsFileRepository.save(materialsFile);
    }

    @Override
    public Optional<MaterialsFile> partialUpdate(MaterialsFile materialsFile) {
        log.debug("Request to partially update MaterialsFile : {}", materialsFile);

        return materialsFileRepository
            .findById(materialsFile.getId())
            .map(existingMaterialsFile -> {
                if (materialsFile.getMaterials() != null) {
                    existingMaterialsFile.setMaterials(materialsFile.getMaterials());
                }

                return existingMaterialsFile;
            })
            .map(materialsFileRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MaterialsFile> findAll(Pageable pageable) {
        log.debug("Request to get all MaterialsFiles");
        return materialsFileRepository.findAll(pageable);
    }

    /**
     *  Get all the materialsFiles where ThemeFile is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MaterialsFile> findAllWhereThemeFileIsNull() {
        log.debug("Request to get all materialsFiles where ThemeFile is null");
        return StreamSupport
            .stream(materialsFileRepository.findAll().spliterator(), false)
            .filter(materialsFile -> materialsFile.getThemeFile() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MaterialsFile> findOne(Long id) {
        log.debug("Request to get MaterialsFile : {}", id);
        return materialsFileRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MaterialsFile : {}", id);
        materialsFileRepository.deleteById(id);
    }
}
