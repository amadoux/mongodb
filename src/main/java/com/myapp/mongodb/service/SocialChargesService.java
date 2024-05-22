package com.myapp.mongodb.service;

import com.myapp.mongodb.service.dto.SocialChargesDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.myapp.mongodb.domain.SocialCharges}.
 */
public interface SocialChargesService {
    /**
     * Save a socialCharges.
     *
     * @param socialChargesDTO the entity to save.
     * @return the persisted entity.
     */
    SocialChargesDTO save(SocialChargesDTO socialChargesDTO);

    /**
     * Updates a socialCharges.
     *
     * @param socialChargesDTO the entity to update.
     * @return the persisted entity.
     */
    SocialChargesDTO update(SocialChargesDTO socialChargesDTO);

    /**
     * Partially updates a socialCharges.
     *
     * @param socialChargesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SocialChargesDTO> partialUpdate(SocialChargesDTO socialChargesDTO);

    /**
     * Get all the socialCharges.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SocialChargesDTO> findAll(Pageable pageable);

    /**
     * Get all the socialCharges with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SocialChargesDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" socialCharges.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SocialChargesDTO> findOne(String id);

    /**
     * Delete the "id" socialCharges.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
