package com.microservices.saliiov.resource.storage.repository;

import com.microservices.saliiov.resource.storage.entity.Storage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Storage entity
 */
@Repository
public interface StorageRepository extends CrudRepository<Storage, Long> {
    Optional<Storage> findByTypeAndBucket(String type, String bucket);
    Optional<Storage> findByType(String type);
}
