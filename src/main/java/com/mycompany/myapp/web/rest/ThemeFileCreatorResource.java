package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ThemeFileCreator;
import com.mycompany.myapp.repository.ThemeFileCreatorRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ThemeFileCreator}.
 */
@RestController
@RequestMapping("/api/theme-file-creators")
@Transactional
public class ThemeFileCreatorResource {

    private final Logger log = LoggerFactory.getLogger(ThemeFileCreatorResource.class);

    private static final String ENTITY_NAME = "themeFileCreator";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ThemeFileCreatorRepository themeFileCreatorRepository;

    public ThemeFileCreatorResource(ThemeFileCreatorRepository themeFileCreatorRepository) {
        this.themeFileCreatorRepository = themeFileCreatorRepository;
    }

    /**
     * {@code POST  /theme-file-creators} : Create a new themeFileCreator.
     *
     * @param themeFileCreator the themeFileCreator to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new themeFileCreator, or with status {@code 400 (Bad Request)} if the themeFileCreator has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ThemeFileCreator> createThemeFileCreator(@RequestBody ThemeFileCreator themeFileCreator)
        throws URISyntaxException {
        log.debug("REST request to save ThemeFileCreator : {}", themeFileCreator);
        if (themeFileCreator.getId() != null) {
            throw new BadRequestAlertException("A new themeFileCreator cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ThemeFileCreator result = themeFileCreatorRepository.save(themeFileCreator);
        return ResponseEntity
            .created(new URI("/api/theme-file-creators/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /theme-file-creators/:id} : Updates an existing themeFileCreator.
     *
     * @param id the id of the themeFileCreator to save.
     * @param themeFileCreator the themeFileCreator to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated themeFileCreator,
     * or with status {@code 400 (Bad Request)} if the themeFileCreator is not valid,
     * or with status {@code 500 (Internal Server Error)} if the themeFileCreator couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ThemeFileCreator> updateThemeFileCreator(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ThemeFileCreator themeFileCreator
    ) throws URISyntaxException {
        log.debug("REST request to update ThemeFileCreator : {}, {}", id, themeFileCreator);
        if (themeFileCreator.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, themeFileCreator.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!themeFileCreatorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ThemeFileCreator result = themeFileCreatorRepository.save(themeFileCreator);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, themeFileCreator.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /theme-file-creators/:id} : Partial updates given fields of an existing themeFileCreator, field will ignore if it is null
     *
     * @param id the id of the themeFileCreator to save.
     * @param themeFileCreator the themeFileCreator to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated themeFileCreator,
     * or with status {@code 400 (Bad Request)} if the themeFileCreator is not valid,
     * or with status {@code 404 (Not Found)} if the themeFileCreator is not found,
     * or with status {@code 500 (Internal Server Error)} if the themeFileCreator couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ThemeFileCreator> partialUpdateThemeFileCreator(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ThemeFileCreator themeFileCreator
    ) throws URISyntaxException {
        log.debug("REST request to partial update ThemeFileCreator partially : {}, {}", id, themeFileCreator);
        if (themeFileCreator.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, themeFileCreator.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!themeFileCreatorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ThemeFileCreator> result = themeFileCreatorRepository
            .findById(themeFileCreator.getId())
            .map(existingThemeFileCreator -> {
                if (themeFileCreator.getFilesAmount() != null) {
                    existingThemeFileCreator.setFilesAmount(themeFileCreator.getFilesAmount());
                }

                return existingThemeFileCreator;
            })
            .map(themeFileCreatorRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, themeFileCreator.getId().toString())
        );
    }

    /**
     * {@code GET  /theme-file-creators} : get all the themeFileCreators.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of themeFileCreators in body.
     */
    @GetMapping("")
    public List<ThemeFileCreator> getAllThemeFileCreators() {
        log.debug("REST request to get all ThemeFileCreators");
        return themeFileCreatorRepository.findAll();
    }

    /**
     * {@code GET  /theme-file-creators/:id} : get the "id" themeFileCreator.
     *
     * @param id the id of the themeFileCreator to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the themeFileCreator, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ThemeFileCreator> getThemeFileCreator(@PathVariable("id") Long id) {
        log.debug("REST request to get ThemeFileCreator : {}", id);
        Optional<ThemeFileCreator> themeFileCreator = themeFileCreatorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(themeFileCreator);
    }

    /**
     * {@code DELETE  /theme-file-creators/:id} : delete the "id" themeFileCreator.
     *
     * @param id the id of the themeFileCreator to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteThemeFileCreator(@PathVariable("id") Long id) {
        log.debug("REST request to delete ThemeFileCreator : {}", id);
        themeFileCreatorRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
