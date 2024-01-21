package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.TestLoader;
import com.mycompany.myapp.repository.TestLoaderRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.TestLoader}.
 */
@RestController
@RequestMapping("/api/test-loaders")
@Transactional
public class TestLoaderResource {

    private final Logger log = LoggerFactory.getLogger(TestLoaderResource.class);

    private static final String ENTITY_NAME = "testLoader";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestLoaderRepository testLoaderRepository;

    public TestLoaderResource(TestLoaderRepository testLoaderRepository) {
        this.testLoaderRepository = testLoaderRepository;
    }

    /**
     * {@code POST  /test-loaders} : Create a new testLoader.
     *
     * @param testLoader the testLoader to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testLoader, or with status {@code 400 (Bad Request)} if the testLoader has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TestLoader> createTestLoader(@RequestBody TestLoader testLoader) throws URISyntaxException {
        log.debug("REST request to save TestLoader : {}", testLoader);
        if (testLoader.getId() != null) {
            throw new BadRequestAlertException("A new testLoader cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestLoader result = testLoaderRepository.save(testLoader);
        return ResponseEntity
            .created(new URI("/api/test-loaders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-loaders/:id} : Updates an existing testLoader.
     *
     * @param id the id of the testLoader to save.
     * @param testLoader the testLoader to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testLoader,
     * or with status {@code 400 (Bad Request)} if the testLoader is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testLoader couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TestLoader> updateTestLoader(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestLoader testLoader
    ) throws URISyntaxException {
        log.debug("REST request to update TestLoader : {}, {}", id, testLoader);
        if (testLoader.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testLoader.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testLoaderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestLoader result = testLoaderRepository.save(testLoader);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testLoader.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-loaders/:id} : Partial updates given fields of an existing testLoader, field will ignore if it is null
     *
     * @param id the id of the testLoader to save.
     * @param testLoader the testLoader to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testLoader,
     * or with status {@code 400 (Bad Request)} if the testLoader is not valid,
     * or with status {@code 404 (Not Found)} if the testLoader is not found,
     * or with status {@code 500 (Internal Server Error)} if the testLoader couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestLoader> partialUpdateTestLoader(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestLoader testLoader
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestLoader partially : {}, {}", id, testLoader);
        if (testLoader.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testLoader.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testLoaderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestLoader> result = testLoaderRepository
            .findById(testLoader.getId())
            .map(existingTestLoader -> {
                if (testLoader.getTestsAmount() != null) {
                    existingTestLoader.setTestsAmount(testLoader.getTestsAmount());
                }

                return existingTestLoader;
            })
            .map(testLoaderRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testLoader.getId().toString())
        );
    }

    /**
     * {@code GET  /test-loaders} : get all the testLoaders.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testLoaders in body.
     */
    @GetMapping("")
    public List<TestLoader> getAllTestLoaders() {
        log.debug("REST request to get all TestLoaders");
        return testLoaderRepository.findAll();
    }

    /**
     * {@code GET  /test-loaders/:id} : get the "id" testLoader.
     *
     * @param id the id of the testLoader to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testLoader, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TestLoader> getTestLoader(@PathVariable("id") Long id) {
        log.debug("REST request to get TestLoader : {}", id);
        Optional<TestLoader> testLoader = testLoaderRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(testLoader);
    }

    /**
     * {@code DELETE  /test-loaders/:id} : delete the "id" testLoader.
     *
     * @param id the id of the testLoader to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestLoader(@PathVariable("id") Long id) {
        log.debug("REST request to delete TestLoader : {}", id);
        testLoaderRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
