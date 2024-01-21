package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.MaterialsFileLoader;
import com.mycompany.myapp.repository.MaterialsFileLoaderRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.MaterialsFileLoader}.
 */
@RestController
@RequestMapping("/api/materials-file-loaders")
@Transactional
public class MaterialsFileLoaderResource {

    private final Logger log = LoggerFactory.getLogger(MaterialsFileLoaderResource.class);

    private static final String ENTITY_NAME = "materialsFileLoader";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaterialsFileLoaderRepository materialsFileLoaderRepository;

    public MaterialsFileLoaderResource(MaterialsFileLoaderRepository materialsFileLoaderRepository) {
        this.materialsFileLoaderRepository = materialsFileLoaderRepository;
    }

    /**
     * {@code POST  /materials-file-loaders} : Create a new materialsFileLoader.
     *
     * @param materialsFileLoader the materialsFileLoader to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new materialsFileLoader, or with status {@code 400 (Bad Request)} if the materialsFileLoader has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MaterialsFileLoader> createMaterialsFileLoader(@RequestBody MaterialsFileLoader materialsFileLoader)
        throws URISyntaxException {
        log.debug("REST request to save MaterialsFileLoader : {}", materialsFileLoader);
        if (materialsFileLoader.getId() != null) {
            throw new BadRequestAlertException("A new materialsFileLoader cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MaterialsFileLoader result = materialsFileLoaderRepository.save(materialsFileLoader);
        return ResponseEntity
            .created(new URI("/api/materials-file-loaders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /materials-file-loaders/:id} : Updates an existing materialsFileLoader.
     *
     * @param id the id of the materialsFileLoader to save.
     * @param materialsFileLoader the materialsFileLoader to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materialsFileLoader,
     * or with status {@code 400 (Bad Request)} if the materialsFileLoader is not valid,
     * or with status {@code 500 (Internal Server Error)} if the materialsFileLoader couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MaterialsFileLoader> updateMaterialsFileLoader(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MaterialsFileLoader materialsFileLoader
    ) throws URISyntaxException {
        log.debug("REST request to update MaterialsFileLoader : {}, {}", id, materialsFileLoader);
        if (materialsFileLoader.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, materialsFileLoader.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!materialsFileLoaderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MaterialsFileLoader result = materialsFileLoaderRepository.save(materialsFileLoader);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, materialsFileLoader.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /materials-file-loaders/:id} : Partial updates given fields of an existing materialsFileLoader, field will ignore if it is null
     *
     * @param id the id of the materialsFileLoader to save.
     * @param materialsFileLoader the materialsFileLoader to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materialsFileLoader,
     * or with status {@code 400 (Bad Request)} if the materialsFileLoader is not valid,
     * or with status {@code 404 (Not Found)} if the materialsFileLoader is not found,
     * or with status {@code 500 (Internal Server Error)} if the materialsFileLoader couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MaterialsFileLoader> partialUpdateMaterialsFileLoader(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MaterialsFileLoader materialsFileLoader
    ) throws URISyntaxException {
        log.debug("REST request to partial update MaterialsFileLoader partially : {}, {}", id, materialsFileLoader);
        if (materialsFileLoader.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, materialsFileLoader.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!materialsFileLoaderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MaterialsFileLoader> result = materialsFileLoaderRepository
            .findById(materialsFileLoader.getId())
            .map(existingMaterialsFileLoader -> {
                if (materialsFileLoader.getFilesAmount() != null) {
                    existingMaterialsFileLoader.setFilesAmount(materialsFileLoader.getFilesAmount());
                }

                return existingMaterialsFileLoader;
            })
            .map(materialsFileLoaderRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, materialsFileLoader.getId().toString())
        );
    }

    /**
     * {@code GET  /materials-file-loaders} : get all the materialsFileLoaders.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of materialsFileLoaders in body.
     */
    @GetMapping("")
    public List<MaterialsFileLoader> getAllMaterialsFileLoaders() {
        log.debug("REST request to get all MaterialsFileLoaders");
        return materialsFileLoaderRepository.findAll();
    }

    /**
     * {@code GET  /materials-file-loaders/:id} : get the "id" materialsFileLoader.
     *
     * @param id the id of the materialsFileLoader to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the materialsFileLoader, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MaterialsFileLoader> getMaterialsFileLoader(@PathVariable("id") Long id) {
        log.debug("REST request to get MaterialsFileLoader : {}", id);
        Optional<MaterialsFileLoader> materialsFileLoader = materialsFileLoaderRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(materialsFileLoader);
    }

    /**
     * {@code DELETE  /materials-file-loaders/:id} : delete the "id" materialsFileLoader.
     *
     * @param id the id of the materialsFileLoader to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterialsFileLoader(@PathVariable("id") Long id) {
        log.debug("REST request to delete MaterialsFileLoader : {}", id);
        materialsFileLoaderRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
