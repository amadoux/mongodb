package com.myapp.mongodb.repository;

import com.myapp.mongodb.domain.Absence;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Absence entity.
 */
@Repository
public interface AbsenceRepository extends MongoRepository<Absence, String> {
    @Query("{}")
    Page<Absence> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Absence> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Absence> findOneWithEagerRelationships(String id);
}
