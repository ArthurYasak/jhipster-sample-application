package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.TestCreator;
import com.mycompany.myapp.repository.TestCreatorRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.TestCreator}.
 */
@RestController
@RequestMapping("/api/test-creators")
@Transactional
public class TestCreatorResource {

    private final Logger log = LoggerFactory.getLogger(TestCreatorResource.class);

    private static final String ENTITY_NAME = "testCreator";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestCreatorRepository testCreatorRepository;

    public TestCreatorResource(TestCreatorRepository testCreatorRepository) {
        this.testCreatorRepository = testCreatorRepository;
    }

    /**
     * {@code POST  /test-creators} : Create a new testCreator.
     *
     * @param testCreator the testCreator to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testCreator, or with status {@code 400 (Bad Request)} if the testCreator has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TestCreator> createTestCreator(@RequestBody TestCreator testCreator) throws URISyntaxException {
        log.debug("REST request to save TestCreator : {}", testCreator);
        if (testCreator.getId() != null) {
            throw new BadRequestAlertException("A new testCreator cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestCreator result = testCreatorRepository.save(testCreator);
        return ResponseEntity
            .created(new URI("/api/test-creators/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-creators/:id} : Updates an existing testCreator.
     *
     * @param id the id of the testCreator to save.
     * @param testCreator the testCreator to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCreator,
     * or with status {@code 400 (Bad Request)} if the testCreator is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testCreator couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TestCreator> updateTestCreator(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestCreator testCreator
    ) throws URISyntaxException {
        log.debug("REST request to update TestCreator : {}, {}", id, testCreator);
        if (testCreator.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCreator.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCreatorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestCreator result = testCreatorRepository.save(testCreator);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testCreator.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-creators/:id} : Partial updates given fields of an existing testCreator, field will ignore if it is null
     *
     * @param id the id of the testCreator to save.
     * @param testCreator the testCreator to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCreator,
     * or with status {@code 400 (Bad Request)} if the testCreator is not valid,
     * or with status {@code 404 (Not Found)} if the testCreator is not found,
     * or with status {@code 500 (Internal Server Error)} if the testCreator couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestCreator> partialUpdateTestCreator(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestCreator testCreator
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestCreator partially : {}, {}", id, testCreator);
        if (testCreator.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCreator.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCreatorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestCreator> result = testCreatorRepository
            .findById(testCreator.getId())
            .map(existingTestCreator -> {
                if (testCreator.getTestsAmount() != null) {
                    existingTestCreator.setTestsAmount(testCreator.getTestsAmount());
                }

                return existingTestCreator;
            })
            .map(testCreatorRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testCreator.getId().toString())
        );
    }

    /**
     * {@code GET  /test-creators} : get all the testCreators.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testCreators in body.
     */
    @GetMapping("")
    public List<TestCreator> getAllTestCreators() {
        log.debug("REST request to get all TestCreators");
        return testCreatorRepository.findAll();
    }

    /**
     * {@code GET  /test-creators/:id} : get the "id" testCreator.
     *
     * @param id the id of the testCreator to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testCreator, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TestCreator> getTestCreator(@PathVariable("id") Long id) {
        log.debug("REST request to get TestCreator : {}", id);
        Optional<TestCreator> testCreator = testCreatorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(testCreator);
    }

    /**
     * {@code DELETE  /test-creators/:id} : delete the "id" testCreator.
     *
     * @param id the id of the testCreator to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestCreator(@PathVariable("id") Long id) {
        log.debug("REST request to delete TestCreator : {}", id);
        testCreatorRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
