package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.TestEntity;
import com.mycompany.myapp.repository.TestEntityRepository;
import com.mycompany.myapp.service.TestEntityService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.TestEntity}.
 */
@RestController
@RequestMapping("/api/test-entities")
public class TestEntityResource {

    private final Logger log = LoggerFactory.getLogger(TestEntityResource.class);

    private static final String ENTITY_NAME = "testEntity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestEntityService testEntityService;

    private final TestEntityRepository testEntityRepository;

    public TestEntityResource(TestEntityService testEntityService, TestEntityRepository testEntityRepository) {
        this.testEntityService = testEntityService;
        this.testEntityRepository = testEntityRepository;
    }

    /**
     * {@code POST  /test-entities} : Create a new testEntity.
     *
     * @param testEntity the testEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testEntity, or with status {@code 400 (Bad Request)} if the testEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TestEntity> createTestEntity(@RequestBody TestEntity testEntity) throws URISyntaxException {
        log.debug("REST request to save TestEntity : {}", testEntity);
        if (testEntity.getId() != null) {
            throw new BadRequestAlertException("A new testEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestEntity result = testEntityService.save(testEntity);
        return ResponseEntity
            .created(new URI("/api/test-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-entities/:id} : Updates an existing testEntity.
     *
     * @param id the id of the testEntity to save.
     * @param testEntity the testEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testEntity,
     * or with status {@code 400 (Bad Request)} if the testEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TestEntity> updateTestEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestEntity testEntity
    ) throws URISyntaxException {
        log.debug("REST request to update TestEntity : {}, {}", id, testEntity);
        if (testEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestEntity result = testEntityService.update(testEntity);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testEntity.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-entities/:id} : Partial updates given fields of an existing testEntity, field will ignore if it is null
     *
     * @param id the id of the testEntity to save.
     * @param testEntity the testEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testEntity,
     * or with status {@code 400 (Bad Request)} if the testEntity is not valid,
     * or with status {@code 404 (Not Found)} if the testEntity is not found,
     * or with status {@code 500 (Internal Server Error)} if the testEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestEntity> partialUpdateTestEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestEntity testEntity
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestEntity partially : {}, {}", id, testEntity);
        if (testEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestEntity> result = testEntityService.partialUpdate(testEntity);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testEntity.getId().toString())
        );
    }

    /**
     * {@code GET  /test-entities} : get all the testEntities.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testEntities in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TestEntity>> getAllTestEntities(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of TestEntities");
        Page<TestEntity> page;
        if (eagerload) {
            page = testEntityService.findAllWithEagerRelationships(pageable);
        } else {
            page = testEntityService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-entities/:id} : get the "id" testEntity.
     *
     * @param id the id of the testEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TestEntity> getTestEntity(@PathVariable("id") Long id) {
        log.debug("REST request to get TestEntity : {}", id);
        Optional<TestEntity> testEntity = testEntityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testEntity);
    }

    /**
     * {@code DELETE  /test-entities/:id} : delete the "id" testEntity.
     *
     * @param id the id of the testEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestEntity(@PathVariable("id") Long id) {
        log.debug("REST request to delete TestEntity : {}", id);
        testEntityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
