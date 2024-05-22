package com.myapp.mongodb.service.impl;

import com.myapp.mongodb.domain.Contract;
import com.myapp.mongodb.repository.ContractRepository;
import com.myapp.mongodb.service.ContractService;
import com.myapp.mongodb.service.dto.ContractDTO;
import com.myapp.mongodb.service.mapper.ContractMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.myapp.mongodb.domain.Contract}.
 */
@Service
public class ContractServiceImpl implements ContractService {

    private final Logger log = LoggerFactory.getLogger(ContractServiceImpl.class);

    private final ContractRepository contractRepository;

    private final ContractMapper contractMapper;

    public ContractServiceImpl(ContractRepository contractRepository, ContractMapper contractMapper) {
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
    }

    @Override
    public ContractDTO save(ContractDTO contractDTO) {
        log.debug("Request to save Contract : {}", contractDTO);
        Contract contract = contractMapper.toEntity(contractDTO);
        contract = contractRepository.save(contract);
        return contractMapper.toDto(contract);
    }

    @Override
    public ContractDTO update(ContractDTO contractDTO) {
        log.debug("Request to update Contract : {}", contractDTO);
        Contract contract = contractMapper.toEntity(contractDTO);
        contract = contractRepository.save(contract);
        return contractMapper.toDto(contract);
    }

    @Override
    public Optional<ContractDTO> partialUpdate(ContractDTO contractDTO) {
        log.debug("Request to partially update Contract : {}", contractDTO);

        return contractRepository
            .findById(contractDTO.getId())
            .map(existingContract -> {
                contractMapper.partialUpdate(existingContract, contractDTO);

                return existingContract;
            })
            .map(contractRepository::save)
            .map(contractMapper::toDto);
    }

    @Override
    public Page<ContractDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Contracts");
        return contractRepository.findAll(pageable).map(contractMapper::toDto);
    }

    public Page<ContractDTO> findAllWithEagerRelationships(Pageable pageable) {
        return contractRepository.findAllWithEagerRelationships(pageable).map(contractMapper::toDto);
    }

    @Override
    public Optional<ContractDTO> findOne(String id) {
        log.debug("Request to get Contract : {}", id);
        return contractRepository.findOneWithEagerRelationships(id).map(contractMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Contract : {}", id);
        contractRepository.deleteById(id);
    }
}
