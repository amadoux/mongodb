package com.myapp.mongodb.web.rest;

import com.myapp.mongodb.repository.SocialChargesRepository;
import com.myapp.mongodb.service.SocialChargesService;
import com.myapp.mongodb.service.dto.SocialChargesDTO;
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
 * REST controller for managing {@link com.myapp.mongodb.domain.SocialCharges}.
 */
@RestController
@RequestMapping("/api/social-charges")
public class SocialChargesResource {

    private final Logger log = LoggerFactory.getLogger(SocialChargesResource.class);

    private static final String ENTITY_NAME = "socialCharges";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SocialChargesService socialChargesService;

    private final SocialChargesRepository socialChargesRepository;

    public SocialChargesResource(SocialChargesService socialChargesService, SocialChargesRepository socialChargesRepository) {
        this.socialChargesService = socialChargesService;
        this.socialChargesRepository = socialChargesRepository;
    }

    /**
     * {@code POST  /social-charges} : Create a new socialCharges.
     *
     * @param socialChargesDTO the socialChargesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new socialChargesDTO, or with status {@code 400 (Bad Request)} if the socialCharges has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SocialChargesDTO> createSocialCharges(@Valid @RequestBody SocialChargesDTO socialChargesDTO)
        throws URISyntaxException {
        log.debug("REST request to save SocialCharges : {}", socialChargesDTO);
        if (socialChargesDTO.getId() != null) {
            throw new BadRequestAlertException("A new socialCharges cannot already have an ID", ENTITY_NAME, "idexists");
        }
        socialChargesDTO = socialChargesService.save(socialChargesDTO);
        return ResponseEntity.created(new URI("/api/social-charges/" + socialChargesDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, socialChargesDTO.getId()))
            .body(socialChargesDTO);
    }

    /**
     * {@code PUT  /social-charges/:id} : Updates an existing socialCharges.
     *
     * @param id the id of the socialChargesDTO to save.
     * @param socialChargesDTO the socialChargesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated socialChargesDTO,
     * or with status {@code 400 (Bad Request)} if the socialChargesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the socialChargesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SocialChargesDTO> updateSocialCharges(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody SocialChargesDTO socialChargesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SocialCharges : {}, {}", id, socialChargesDTO);
        if (socialChargesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, socialChargesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!socialChargesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        socialChargesDTO = socialChargesService.update(socialChargesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, socialChargesDTO.getId()))
            .body(socialChargesDTO);
    }

    /**
     * {@code PATCH  /social-charges/:id} : Partial updates given fields of an existing socialCharges, field will ignore if it is null
     *
     * @param id the id of the socialChargesDTO to save.
     * @param socialChargesDTO the socialChargesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated socialChargesDTO,
     * or with status {@code 400 (Bad Request)} if the socialChargesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the socialChargesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the socialChargesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SocialChargesDTO> partialUpdateSocialCharges(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody SocialChargesDTO socialChargesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SocialCharges partially : {}, {}", id, socialChargesDTO);
        if (socialChargesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, socialChargesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!socialChargesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SocialChargesDTO> result = socialChargesService.partialUpdate(socialChargesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, socialChargesDTO.getId())
        );
    }

    /**
     * {@code GET  /social-charges} : get all the socialCharges.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of socialCharges in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SocialChargesDTO>> getAllSocialCharges(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of SocialCharges");
        Page<SocialChargesDTO> page;
        if (eagerload) {
            page = socialChargesService.findAllWithEagerRelationships(pageable);
        } else {
            page = socialChargesService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /social-charges/:id} : get the "id" socialCharges.
     *
     * @param id the id of the socialChargesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the socialChargesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SocialChargesDTO> getSocialCharges(@PathVariable("id") String id) {
        log.debug("REST request to get SocialCharges : {}", id);
        Optional<SocialChargesDTO> socialChargesDTO = socialChargesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(socialChargesDTO);
    }

    /**
     * {@code DELETE  /social-charges/:id} : delete the "id" socialCharges.
     *
     * @param id the id of the socialChargesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSocialCharges(@PathVariable("id") String id) {
        log.debug("REST request to delete SocialCharges : {}", id);
        socialChargesService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
