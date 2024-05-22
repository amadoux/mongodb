package com.myapp.mongodb.repository;

import com.myapp.mongodb.domain.PaySlip;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the PaySlip entity.
 */
@Repository
public interface PaySlipRepository extends MongoRepository<PaySlip, String> {
    @Query("{}")
    Page<PaySlip> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<PaySlip> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<PaySlip> findOneWithEagerRelationships(String id);
}
