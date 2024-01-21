package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.StatisticGenerator;
import com.mycompany.myapp.repository.StatisticGeneratorRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.StatisticGenerator}.
 */
@RestController
@RequestMapping("/api/statistic-generators")
@Transactional
public class StatisticGeneratorResource {

    private final Logger log = LoggerFactory.getLogger(StatisticGeneratorResource.class);

    private static final String ENTITY_NAME = "statisticGenerator";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StatisticGeneratorRepository statisticGeneratorRepository;

    public StatisticGeneratorResource(StatisticGeneratorRepository statisticGeneratorRepository) {
        this.statisticGeneratorRepository = statisticGeneratorRepository;
    }

    /**
     * {@code POST  /statistic-generators} : Create a new statisticGenerator.
     *
     * @param statisticGenerator the statisticGenerator to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new statisticGenerator, or with status {@code 400 (Bad Request)} if the statisticGenerator has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StatisticGenerator> createStatisticGenerator(@RequestBody StatisticGenerator statisticGenerator)
        throws URISyntaxException {
        log.debug("REST request to save StatisticGenerator : {}", statisticGenerator);
        if (statisticGenerator.getId() != null) {
            throw new BadRequestAlertException("A new statisticGenerator cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StatisticGenerator result = statisticGeneratorRepository.save(statisticGenerator);
        return ResponseEntity
            .created(new URI("/api/statistic-generators/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /statistic-generators/:id} : Updates an existing statisticGenerator.
     *
     * @param id the id of the statisticGenerator to save.
     * @param statisticGenerator the statisticGenerator to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated statisticGenerator,
     * or with status {@code 400 (Bad Request)} if the statisticGenerator is not valid,
     * or with status {@code 500 (Internal Server Error)} if the statisticGenerator couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StatisticGenerator> updateStatisticGenerator(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StatisticGenerator statisticGenerator
    ) throws URISyntaxException {
        log.debug("REST request to update StatisticGenerator : {}, {}", id, statisticGenerator);
        if (statisticGenerator.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, statisticGenerator.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!statisticGeneratorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StatisticGenerator result = statisticGeneratorRepository.save(statisticGenerator);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, statisticGenerator.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /statistic-generators/:id} : Partial updates given fields of an existing statisticGenerator, field will ignore if it is null
     *
     * @param id the id of the statisticGenerator to save.
     * @param statisticGenerator the statisticGenerator to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated statisticGenerator,
     * or with status {@code 400 (Bad Request)} if the statisticGenerator is not valid,
     * or with status {@code 404 (Not Found)} if the statisticGenerator is not found,
     * or with status {@code 500 (Internal Server Error)} if the statisticGenerator couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StatisticGenerator> partialUpdateStatisticGenerator(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StatisticGenerator statisticGenerator
    ) throws URISyntaxException {
        log.debug("REST request to partial update StatisticGenerator partially : {}, {}", id, statisticGenerator);
        if (statisticGenerator.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, statisticGenerator.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!statisticGeneratorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StatisticGenerator> result = statisticGeneratorRepository
            .findById(statisticGenerator.getId())
            .map(existingStatisticGenerator -> {
                if (statisticGenerator.getGeneratedReportsAmount() != null) {
                    existingStatisticGenerator.setGeneratedReportsAmount(statisticGenerator.getGeneratedReportsAmount());
                }

                return existingStatisticGenerator;
            })
            .map(statisticGeneratorRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, statisticGenerator.getId().toString())
        );
    }

    /**
     * {@code GET  /statistic-generators} : get all the statisticGenerators.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of statisticGenerators in body.
     */
    @GetMapping("")
    public List<StatisticGenerator> getAllStatisticGenerators() {
        log.debug("REST request to get all StatisticGenerators");
        return statisticGeneratorRepository.findAll();
    }

    /**
     * {@code GET  /statistic-generators/:id} : get the "id" statisticGenerator.
     *
     * @param id the id of the statisticGenerator to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the statisticGenerator, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StatisticGenerator> getStatisticGenerator(@PathVariable("id") Long id) {
        log.debug("REST request to get StatisticGenerator : {}", id);
        Optional<StatisticGenerator> statisticGenerator = statisticGeneratorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(statisticGenerator);
    }

    /**
     * {@code DELETE  /statistic-generators/:id} : delete the "id" statisticGenerator.
     *
     * @param id the id of the statisticGenerator to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatisticGenerator(@PathVariable("id") Long id) {
        log.debug("REST request to delete StatisticGenerator : {}", id);
        statisticGeneratorRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
