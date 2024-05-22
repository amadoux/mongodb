package com.myapp.mongodb.repository;

import com.myapp.mongodb.domain.Enterprise;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Enterprise entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnterpriseRepository extends MongoRepository<Enterprise, String> {}
