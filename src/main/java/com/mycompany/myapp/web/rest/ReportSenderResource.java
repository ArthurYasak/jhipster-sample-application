package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ReportSender;
import com.mycompany.myapp.repository.ReportSenderRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ReportSender}.
 */
@RestController
@RequestMapping("/api/report-senders")
@Transactional
public class ReportSenderResource {

    private final Logger log = LoggerFactory.getLogger(ReportSenderResource.class);

    private static final String ENTITY_NAME = "reportSender";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportSenderRepository reportSenderRepository;

    public ReportSenderResource(ReportSenderRepository reportSenderRepository) {
        this.reportSenderRepository = reportSenderRepository;
    }

    /**
     * {@code POST  /report-senders} : Create a new reportSender.
     *
     * @param reportSender the reportSender to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportSender, or with status {@code 400 (Bad Request)} if the reportSender has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReportSender> createReportSender(@RequestBody ReportSender reportSender) throws URISyntaxException {
        log.debug("REST request to save ReportSender : {}", reportSender);
        if (reportSender.getId() != null) {
            throw new BadRequestAlertException("A new reportSender cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReportSender result = reportSenderRepository.save(reportSender);
        return ResponseEntity
            .created(new URI("/api/report-senders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /report-senders/:id} : Updates an existing reportSender.
     *
     * @param id the id of the reportSender to save.
     * @param reportSender the reportSender to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportSender,
     * or with status {@code 400 (Bad Request)} if the reportSender is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportSender couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReportSender> updateReportSender(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReportSender reportSender
    ) throws URISyntaxException {
        log.debug("REST request to update ReportSender : {}, {}", id, reportSender);
        if (reportSender.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportSender.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportSenderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReportSender result = reportSenderRepository.save(reportSender);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportSender.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /report-senders/:id} : Partial updates given fields of an existing reportSender, field will ignore if it is null
     *
     * @param id the id of the reportSender to save.
     * @param reportSender the reportSender to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportSender,
     * or with status {@code 400 (Bad Request)} if the reportSender is not valid,
     * or with status {@code 404 (Not Found)} if the reportSender is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportSender couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportSender> partialUpdateReportSender(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReportSender reportSender
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReportSender partially : {}, {}", id, reportSender);
        if (reportSender.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportSender.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportSenderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportSender> result = reportSenderRepository
            .findById(reportSender.getId())
            .map(existingReportSender -> {
                if (reportSender.getEmailList() != null) {
                    existingReportSender.setEmailList(reportSender.getEmailList());
                }

                return existingReportSender;
            })
            .map(reportSenderRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportSender.getId().toString())
        );
    }

    /**
     * {@code GET  /report-senders} : get all the reportSenders.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportSenders in body.
     */
    @GetMapping("")
    public List<ReportSender> getAllReportSenders() {
        log.debug("REST request to get all ReportSenders");
        return reportSenderRepository.findAll();
    }

    /**
     * {@code GET  /report-senders/:id} : get the "id" reportSender.
     *
     * @param id the id of the reportSender to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportSender, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReportSender> getReportSender(@PathVariable("id") Long id) {
        log.debug("REST request to get ReportSender : {}", id);
        Optional<ReportSender> reportSender = reportSenderRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(reportSender);
    }

    /**
     * {@code DELETE  /report-senders/:id} : delete the "id" reportSender.
     *
     * @param id the id of the reportSender to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReportSender(@PathVariable("id") Long id) {
        log.debug("REST request to delete ReportSender : {}", id);
        reportSenderRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
