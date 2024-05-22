package com.myapp.mongodb.repository;

import com.myapp.mongodb.domain.SocialCharges;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the SocialCharges entity.
 */
@Repository
public interface SocialChargesRepository extends MongoRepository<SocialCharges, String> {
    @Query("{}")
    Page<SocialCharges> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<SocialCharges> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<SocialCharges> findOneWithEagerRelationships(String id);
}
