package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.MaterialsFile;
import com.mycompany.myapp.repository.MaterialsFileRepository;
import com.mycompany.myapp.service.MaterialsFileService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.MaterialsFile}.
 */
@RestController
@RequestMapping("/api/materials-files")
public class MaterialsFileResource {

    private final Logger log = LoggerFactory.getLogger(MaterialsFileResource.class);

    private static final String ENTITY_NAME = "materialsFile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaterialsFileService materialsFileService;

    private final MaterialsFileRepository materialsFileRepository;

    public MaterialsFileResource(MaterialsFileService materialsFileService, MaterialsFileRepository materialsFileRepository) {
        this.materialsFileService = materialsFileService;
        this.materialsFileRepository = materialsFileRepository;
    }

    /**
     * {@code POST  /materials-files} : Create a new materialsFile.
     *
     * @param materialsFile the materialsFile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new materialsFile, or with status {@code 400 (Bad Request)} if the materialsFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MaterialsFile> createMaterialsFile(@RequestBody MaterialsFile materialsFile) throws URISyntaxException {
        log.debug("REST request to save MaterialsFile : {}", materialsFile);
        if (materialsFile.getId() != null) {
            throw new BadRequestAlertException("A new materialsFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MaterialsFile result = materialsFileService.save(materialsFile);
        return ResponseEntity
            .created(new URI("/api/materials-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /materials-files/:id} : Updates an existing materialsFile.
     *
     * @param id the id of the materialsFile to save.
     * @param materialsFile the materialsFile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materialsFile,
     * or with status {@code 400 (Bad Request)} if the materialsFile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the materialsFile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MaterialsFile> updateMaterialsFile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MaterialsFile materialsFile
    ) throws URISyntaxException {
        log.debug("REST request to update MaterialsFile : {}, {}", id, materialsFile);
        if (materialsFile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, materialsFile.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!materialsFileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MaterialsFile result = materialsFileService.update(materialsFile);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, materialsFile.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /materials-files/:id} : Partial updates given fields of an existing materialsFile, field will ignore if it is null
     *
     * @param id the id of the materialsFile to save.
     * @param materialsFile the materialsFile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materialsFile,
     * or with status {@code 400 (Bad Request)} if the materialsFile is not valid,
     * or with status {@code 404 (Not Found)} if the materialsFile is not found,
     * or with status {@code 500 (Internal Server Error)} if the materialsFile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MaterialsFile> partialUpdateMaterialsFile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MaterialsFile materialsFile
    ) throws URISyntaxException {
        log.debug("REST request to partial update MaterialsFile partially : {}, {}", id, materialsFile);
        if (materialsFile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, materialsFile.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!materialsFileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MaterialsFile> result = materialsFileService.partialUpdate(materialsFile);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, materialsFile.getId().toString())
        );
    }

    /**
     * {@code GET  /materials-files} : get all the materialsFiles.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of materialsFiles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MaterialsFile>> getAllMaterialsFiles(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("themefile-is-null".equals(filter)) {
            log.debug("REST request to get all MaterialsFiles where themeFile is null");
            return new ResponseEntity<>(materialsFileService.findAllWhereThemeFileIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of MaterialsFiles");
        Page<MaterialsFile> page = materialsFileService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /materials-files/:id} : get the "id" materialsFile.
     *
     * @param id the id of the materialsFile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the materialsFile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MaterialsFile> getMaterialsFile(@PathVariable("id") Long id) {
        log.debug("REST request to get MaterialsFile : {}", id);
        Optional<MaterialsFile> materialsFile = materialsFileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(materialsFile);
    }

    /**
     * {@code DELETE  /materials-files/:id} : delete the "id" materialsFile.
     *
     * @param id the id of the materialsFile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterialsFile(@PathVariable("id") Long id) {
        log.debug("REST request to delete MaterialsFile : {}", id);
        materialsFileService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
