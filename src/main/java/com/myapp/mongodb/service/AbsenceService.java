package com.myapp.mongodb.service;

import com.myapp.mongodb.service.dto.AbsenceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.myapp.mongodb.domain.Absence}.
 */
public interface AbsenceService {
    /**
     * Save a absence.
     *
     * @param absenceDTO the entity to save.
     * @return the persisted entity.
     */
    AbsenceDTO save(AbsenceDTO absenceDTO);

    /**
     * Updates a absence.
     *
     * @param absenceDTO the entity to update.
     * @return the persisted entity.
     */
    AbsenceDTO update(AbsenceDTO absenceDTO);

    /**
     * Partially updates a absence.
     *
     * @param absenceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AbsenceDTO> partialUpdate(AbsenceDTO absenceDTO);

    /**
     * Get all the absences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AbsenceDTO> findAll(Pageable pageable);

    /**
     * Get all the absences with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AbsenceDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" absence.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AbsenceDTO> findOne(String id);

    /**
     * Delete the "id" absence.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
