package com.microservices.saliiov.resource.resource_service.repository;

import com.microservices.saliiov.resource.resource_service.entity.Resource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Resource entity
 */
@Repository
public interface ResourceRepository extends CrudRepository<Resource, Long> {

}
