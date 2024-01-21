package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.ThemeFile;
import com.mycompany.myapp.repository.ThemeFileRepository;
import com.mycompany.myapp.service.ThemeFileService;
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
 * Service Implementation for managing {@link com.mycompany.myapp.domain.ThemeFile}.
 */
@Service
@Transactional
public class ThemeFileServiceImpl implements ThemeFileService {

    private final Logger log = LoggerFactory.getLogger(ThemeFileServiceImpl.class);

    private final ThemeFileRepository themeFileRepository;

    public ThemeFileServiceImpl(ThemeFileRepository themeFileRepository) {
        this.themeFileRepository = themeFileRepository;
    }

    @Override
    public ThemeFile save(ThemeFile themeFile) {
        log.debug("Request to save ThemeFile : {}", themeFile);
        return themeFileRepository.save(themeFile);
    }

    @Override
    public ThemeFile update(ThemeFile themeFile) {
        log.debug("Request to update ThemeFile : {}", themeFile);
        return themeFileRepository.save(themeFile);
    }

    @Override
    public Optional<ThemeFile> partialUpdate(ThemeFile themeFile) {
        log.debug("Request to partially update ThemeFile : {}", themeFile);

        return themeFileRepository
            .findById(themeFile.getId())
            .map(existingThemeFile -> {
                if (themeFile.getTheme() != null) {
                    existingThemeFile.setTheme(themeFile.getTheme());
                }

                return existingThemeFile;
            })
            .map(themeFileRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ThemeFile> findAll(Pageable pageable) {
        log.debug("Request to get all ThemeFiles");
        return themeFileRepository.findAll(pageable);
    }

    public Page<ThemeFile> findAllWithEagerRelationships(Pageable pageable) {
        return themeFileRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     *  Get all the themeFiles where TestEntity is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ThemeFile> findAllWhereTestEntityIsNull() {
        log.debug("Request to get all themeFiles where TestEntity is null");
        return StreamSupport
            .stream(themeFileRepository.findAll().spliterator(), false)
            .filter(themeFile -> themeFile.getTestEntity() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ThemeFile> findOne(Long id) {
        log.debug("Request to get ThemeFile : {}", id);
        return themeFileRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ThemeFile : {}", id);
        themeFileRepository.deleteById(id);
    }
}
