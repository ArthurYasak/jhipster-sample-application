package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ThemeFile;
import com.mycompany.myapp.repository.ThemeFileRepository;
import com.mycompany.myapp.service.ThemeFileService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ThemeFile}.
 */
@RestController
@RequestMapping("/api/theme-files")
public class ThemeFileResource {

    private final Logger log = LoggerFactory.getLogger(ThemeFileResource.class);

    private static final String ENTITY_NAME = "themeFile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ThemeFileService themeFileService;

    private final ThemeFileRepository themeFileRepository;

    public ThemeFileResource(ThemeFileService themeFileService, ThemeFileRepository themeFileRepository) {
        this.themeFileService = themeFileService;
        this.themeFileRepository = themeFileRepository;
    }

    /**
     * {@code POST  /theme-files} : Create a new themeFile.
     *
     * @param themeFile the themeFile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new themeFile, or with status {@code 400 (Bad Request)} if the themeFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ThemeFile> createThemeFile(@RequestBody ThemeFile themeFile) throws URISyntaxException {
        log.debug("REST request to save ThemeFile : {}", themeFile);
        if (themeFile.getId() != null) {
            throw new BadRequestAlertException("A new themeFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ThemeFile result = themeFileService.save(themeFile);
        return ResponseEntity
            .created(new URI("/api/theme-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /theme-files/:id} : Updates an existing themeFile.
     *
     * @param id the id of the themeFile to save.
     * @param themeFile the themeFile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated themeFile,
     * or with status {@code 400 (Bad Request)} if the themeFile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the themeFile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ThemeFile> updateThemeFile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ThemeFile themeFile
    ) throws URISyntaxException {
        log.debug("REST request to update ThemeFile : {}, {}", id, themeFile);
        if (themeFile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, themeFile.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!themeFileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ThemeFile result = themeFileService.update(themeFile);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, themeFile.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /theme-files/:id} : Partial updates given fields of an existing themeFile, field will ignore if it is null
     *
     * @param id the id of the themeFile to save.
     * @param themeFile the themeFile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated themeFile,
     * or with status {@code 400 (Bad Request)} if the themeFile is not valid,
     * or with status {@code 404 (Not Found)} if the themeFile is not found,
     * or with status {@code 500 (Internal Server Error)} if the themeFile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ThemeFile> partialUpdateThemeFile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ThemeFile themeFile
    ) throws URISyntaxException {
        log.debug("REST request to partial update ThemeFile partially : {}, {}", id, themeFile);
        if (themeFile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, themeFile.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!themeFileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ThemeFile> result = themeFileService.partialUpdate(themeFile);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, themeFile.getId().toString())
        );
    }

    /**
     * {@code GET  /theme-files} : get all the themeFiles.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of themeFiles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ThemeFile>> getAllThemeFiles(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("testentity-is-null".equals(filter)) {
            log.debug("REST request to get all ThemeFiles where testEntity is null");
            return new ResponseEntity<>(themeFileService.findAllWhereTestEntityIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of ThemeFiles");
        Page<ThemeFile> page;
        if (eagerload) {
            page = themeFileService.findAllWithEagerRelationships(pageable);
        } else {
            page = themeFileService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /theme-files/:id} : get the "id" themeFile.
     *
     * @param id the id of the themeFile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the themeFile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ThemeFile> getThemeFile(@PathVariable("id") Long id) {
        log.debug("REST request to get ThemeFile : {}", id);
        Optional<ThemeFile> themeFile = themeFileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(themeFile);
    }

    /**
     * {@code DELETE  /theme-files/:id} : delete the "id" themeFile.
     *
     * @param id the id of the themeFile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteThemeFile(@PathVariable("id") Long id) {
        log.debug("REST request to delete ThemeFile : {}", id);
        themeFileService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
