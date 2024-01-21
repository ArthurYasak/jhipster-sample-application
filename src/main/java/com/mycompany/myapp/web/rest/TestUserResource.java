package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.TestUser;
import com.mycompany.myapp.repository.TestUserRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.TestUser}.
 */
@RestController
@RequestMapping("/api/test-users")
@Transactional
public class TestUserResource {

    private final Logger log = LoggerFactory.getLogger(TestUserResource.class);

    private static final String ENTITY_NAME = "testUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestUserRepository testUserRepository;

    public TestUserResource(TestUserRepository testUserRepository) {
        this.testUserRepository = testUserRepository;
    }

    /**
     * {@code POST  /test-users} : Create a new testUser.
     *
     * @param testUser the testUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testUser, or with status {@code 400 (Bad Request)} if the testUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TestUser> createTestUser(@RequestBody TestUser testUser) throws URISyntaxException {
        log.debug("REST request to save TestUser : {}", testUser);
        if (testUser.getId() != null) {
            throw new BadRequestAlertException("A new testUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestUser result = testUserRepository.save(testUser);
        return ResponseEntity
            .created(new URI("/api/test-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-users/:id} : Updates an existing testUser.
     *
     * @param id the id of the testUser to save.
     * @param testUser the testUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testUser,
     * or with status {@code 400 (Bad Request)} if the testUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TestUser> updateTestUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestUser testUser
    ) throws URISyntaxException {
        log.debug("REST request to update TestUser : {}, {}", id, testUser);
        if (testUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestUser result = testUserRepository.save(testUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-users/:id} : Partial updates given fields of an existing testUser, field will ignore if it is null
     *
     * @param id the id of the testUser to save.
     * @param testUser the testUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testUser,
     * or with status {@code 400 (Bad Request)} if the testUser is not valid,
     * or with status {@code 404 (Not Found)} if the testUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the testUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestUser> partialUpdateTestUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestUser testUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestUser partially : {}, {}", id, testUser);
        if (testUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestUser> result = testUserRepository
            .findById(testUser.getId())
            .map(existingTestUser -> {
                if (testUser.getMarks() != null) {
                    existingTestUser.setMarks(testUser.getMarks());
                }

                return existingTestUser;
            })
            .map(testUserRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testUser.getId().toString())
        );
    }

    /**
     * {@code GET  /test-users} : get all the testUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testUsers in body.
     */
    @GetMapping("")
    public List<TestUser> getAllTestUsers() {
        log.debug("REST request to get all TestUsers");
        return testUserRepository.findAll();
    }

    /**
     * {@code GET  /test-users/:id} : get the "id" testUser.
     *
     * @param id the id of the testUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TestUser> getTestUser(@PathVariable("id") Long id) {
        log.debug("REST request to get TestUser : {}", id);
        Optional<TestUser> testUser = testUserRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(testUser);
    }

    /**
     * {@code DELETE  /test-users/:id} : delete the "id" testUser.
     *
     * @param id the id of the testUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestUser(@PathVariable("id") Long id) {
        log.debug("REST request to delete TestUser : {}", id);
        testUserRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
