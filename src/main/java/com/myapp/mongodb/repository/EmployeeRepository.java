package com.myapp.mongodb.repository;

import com.myapp.mongodb.domain.Employee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Employee entity.
 */
@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {
    @Query("{}")
    Page<Employee> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Employee> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Employee> findOneWithEagerRelationships(String id);
}
