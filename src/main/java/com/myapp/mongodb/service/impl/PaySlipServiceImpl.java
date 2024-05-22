package com.myapp.mongodb.service.impl;

import com.myapp.mongodb.domain.PaySlip;
import com.myapp.mongodb.repository.PaySlipRepository;
import com.myapp.mongodb.service.PaySlipService;
import com.myapp.mongodb.service.dto.PaySlipDTO;
import com.myapp.mongodb.service.mapper.PaySlipMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.myapp.mongodb.domain.PaySlip}.
 */
@Service
public class PaySlipServiceImpl implements PaySlipService {

    private final Logger log = LoggerFactory.getLogger(PaySlipServiceImpl.class);

    private final PaySlipRepository paySlipRepository;

    private final PaySlipMapper paySlipMapper;

    public PaySlipServiceImpl(PaySlipRepository paySlipRepository, PaySlipMapper paySlipMapper) {
        this.paySlipRepository = paySlipRepository;
        this.paySlipMapper = paySlipMapper;
    }

    @Override
    public PaySlipDTO save(PaySlipDTO paySlipDTO) {
        log.debug("Request to save PaySlip : {}", paySlipDTO);
        PaySlip paySlip = paySlipMapper.toEntity(paySlipDTO);
        paySlip = paySlipRepository.save(paySlip);
        return paySlipMapper.toDto(paySlip);
    }

    @Override
    public PaySlipDTO update(PaySlipDTO paySlipDTO) {
        log.debug("Request to update PaySlip : {}", paySlipDTO);
        PaySlip paySlip = paySlipMapper.toEntity(paySlipDTO);
        paySlip = paySlipRepository.save(paySlip);
        return paySlipMapper.toDto(paySlip);
    }

    @Override
    public Optional<PaySlipDTO> partialUpdate(PaySlipDTO paySlipDTO) {
        log.debug("Request to partially update PaySlip : {}", paySlipDTO);

        return paySlipRepository
            .findById(paySlipDTO.getId())
            .map(existingPaySlip -> {
                paySlipMapper.partialUpdate(existingPaySlip, paySlipDTO);

                return existingPaySlip;
            })
            .map(paySlipRepository::save)
            .map(paySlipMapper::toDto);
    }

    @Override
    public Page<PaySlipDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaySlips");
        return paySlipRepository.findAll(pageable).map(paySlipMapper::toDto);
    }

    public Page<PaySlipDTO> findAllWithEagerRelationships(Pageable pageable) {
        return paySlipRepository.findAllWithEagerRelationships(pageable).map(paySlipMapper::toDto);
    }

    @Override
    public Optional<PaySlipDTO> findOne(String id) {
        log.debug("Request to get PaySlip : {}", id);
        return paySlipRepository.findOneWithEagerRelationships(id).map(paySlipMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete PaySlip : {}", id);
        paySlipRepository.deleteById(id);
    }
}
