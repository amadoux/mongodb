package com.myapp.mongodb.service;

import com.myapp.mongodb.service.dto.ContractDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.myapp.mongodb.domain.Contract}.
 */
public interface ContractService {
    /**
     * Save a contract.
     *
     * @param contractDTO the entity to save.
     * @return the persisted entity.
     */
    ContractDTO save(ContractDTO contractDTO);

    /**
     * Updates a contract.
     *
     * @param contractDTO the entity to update.
     * @return the persisted entity.
     */
    ContractDTO update(ContractDTO contractDTO);

    /**
     * Partially updates a contract.
     *
     * @param contractDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContractDTO> partialUpdate(ContractDTO contractDTO);

    /**
     * Get all the contracts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContractDTO> findAll(Pageable pageable);

    /**
     * Get all the contracts with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContractDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" contract.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContractDTO> findOne(String id);

    /**
     * Delete the "id" contract.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
