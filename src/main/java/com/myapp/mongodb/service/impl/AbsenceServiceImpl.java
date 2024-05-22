package com.myapp.mongodb.service.impl;

import com.myapp.mongodb.domain.Absence;
import com.myapp.mongodb.repository.AbsenceRepository;
import com.myapp.mongodb.service.AbsenceService;
import com.myapp.mongodb.service.dto.AbsenceDTO;
import com.myapp.mongodb.service.mapper.AbsenceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.myapp.mongodb.domain.Absence}.
 */
@Service
public class AbsenceServiceImpl implements AbsenceService {

    private final Logger log = LoggerFactory.getLogger(AbsenceServiceImpl.class);

    private final AbsenceRepository absenceRepository;

    private final AbsenceMapper absenceMapper;

    public AbsenceServiceImpl(AbsenceRepository absenceRepository, AbsenceMapper absenceMapper) {
        this.absenceRepository = absenceRepository;
        this.absenceMapper = absenceMapper;
    }

    @Override
    public AbsenceDTO save(AbsenceDTO absenceDTO) {
        log.debug("Request to save Absence : {}", absenceDTO);
        Absence absence = absenceMapper.toEntity(absenceDTO);
        absence = absenceRepository.save(absence);
        return absenceMapper.toDto(absence);
    }

    @Override
    public AbsenceDTO update(AbsenceDTO absenceDTO) {
        log.debug("Request to update Absence : {}", absenceDTO);
        Absence absence = absenceMapper.toEntity(absenceDTO);
        absence = absenceRepository.save(absence);
        return absenceMapper.toDto(absence);
    }

    @Override
    public Optional<AbsenceDTO> partialUpdate(AbsenceDTO absenceDTO) {
        log.debug("Request to partially update Absence : {}", absenceDTO);

        return absenceRepository
            .findById(absenceDTO.getId())
            .map(existingAbsence -> {
                absenceMapper.partialUpdate(existingAbsence, absenceDTO);

                return existingAbsence;
            })
            .map(absenceRepository::save)
            .map(absenceMapper::toDto);
    }

    @Override
    public Page<AbsenceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Absences");
        return absenceRepository.findAll(pageable).map(absenceMapper::toDto);
    }

    public Page<AbsenceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return absenceRepository.findAllWithEagerRelationships(pageable).map(absenceMapper::toDto);
    }

    @Override
    public Optional<AbsenceDTO> findOne(String id) {
        log.debug("Request to get Absence : {}", id);
        return absenceRepository.findOneWithEagerRelationships(id).map(absenceMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Absence : {}", id);
        absenceRepository.deleteById(id);
    }
}
