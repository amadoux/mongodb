package com.myapp.mongodb.web.rest;

import com.myapp.mongodb.repository.PaySlipRepository;
import com.myapp.mongodb.service.PaySlipService;
import com.myapp.mongodb.service.dto.PaySlipDTO;
import com.myapp.mongodb.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.myapp.mongodb.domain.PaySlip}.
 */
@RestController
@RequestMapping("/api/pay-slips")
public class PaySlipResource {

    private final Logger log = LoggerFactory.getLogger(PaySlipResource.class);

    private static final String ENTITY_NAME = "paySlip";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaySlipService paySlipService;

    private final PaySlipRepository paySlipRepository;

    public PaySlipResource(PaySlipService paySlipService, PaySlipRepository paySlipRepository) {
        this.paySlipService = paySlipService;
        this.paySlipRepository = paySlipRepository;
    }

    /**
     * {@code POST  /pay-slips} : Create a new paySlip.
     *
     * @param paySlipDTO the paySlipDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paySlipDTO, or with status {@code 400 (Bad Request)} if the paySlip has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaySlipDTO> createPaySlip(@Valid @RequestBody PaySlipDTO paySlipDTO) throws URISyntaxException {
        log.debug("REST request to save PaySlip : {}", paySlipDTO);
        if (paySlipDTO.getId() != null) {
            throw new BadRequestAlertException("A new paySlip cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paySlipDTO = paySlipService.save(paySlipDTO);
        return ResponseEntity.created(new URI("/api/pay-slips/" + paySlipDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paySlipDTO.getId()))
            .body(paySlipDTO);
    }

    /**
     * {@code PUT  /pay-slips/:id} : Updates an existing paySlip.
     *
     * @param id the id of the paySlipDTO to save.
     * @param paySlipDTO the paySlipDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paySlipDTO,
     * or with status {@code 400 (Bad Request)} if the paySlipDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paySlipDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaySlipDTO> updatePaySlip(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody PaySlipDTO paySlipDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PaySlip : {}, {}", id, paySlipDTO);
        if (paySlipDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paySlipDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paySlipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paySlipDTO = paySlipService.update(paySlipDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paySlipDTO.getId()))
            .body(paySlipDTO);
    }

    /**
     * {@code PATCH  /pay-slips/:id} : Partial updates given fields of an existing paySlip, field will ignore if it is null
     *
     * @param id the id of the paySlipDTO to save.
     * @param paySlipDTO the paySlipDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paySlipDTO,
     * or with status {@code 400 (Bad Request)} if the paySlipDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paySlipDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paySlipDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaySlipDTO> partialUpdatePaySlip(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody PaySlipDTO paySlipDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaySlip partially : {}, {}", id, paySlipDTO);
        if (paySlipDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paySlipDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paySlipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaySlipDTO> result = paySlipService.partialUpdate(paySlipDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paySlipDTO.getId())
        );
    }

    /**
     * {@code GET  /pay-slips} : get all the paySlips.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paySlips in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PaySlipDTO>> getAllPaySlips(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of PaySlips");
        Page<PaySlipDTO> page;
        if (eagerload) {
            page = paySlipService.findAllWithEagerRelationships(pageable);
        } else {
            page = paySlipService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pay-slips/:id} : get the "id" paySlip.
     *
     * @param id the id of the paySlipDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paySlipDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaySlipDTO> getPaySlip(@PathVariable("id") String id) {
        log.debug("REST request to get PaySlip : {}", id);
        Optional<PaySlipDTO> paySlipDTO = paySlipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paySlipDTO);
    }

    /**
     * {@code DELETE  /pay-slips/:id} : delete the "id" paySlip.
     *
     * @param id the id of the paySlipDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaySlip(@PathVariable("id") String id) {
        log.debug("REST request to delete PaySlip : {}", id);
        paySlipService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
