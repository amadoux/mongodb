package com.myapp.mongodb.repository;

import com.myapp.mongodb.domain.Contract;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Contract entity.
 */
@Repository
public interface ContractRepository extends MongoRepository<Contract, String> {
    @Query("{}")
    Page<Contract> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Contract> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Contract> findOneWithEagerRelationships(String id);
}
