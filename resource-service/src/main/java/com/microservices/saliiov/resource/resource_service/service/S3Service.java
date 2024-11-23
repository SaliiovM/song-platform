package com.microservices.saliiov.resource.resource_service.service;

/**
 * Service for S3 operations
 */
public interface S3Service {

    /**
     * Upload file to S3
     * @param data - file data
     * @return - key of uploaded file
     */
    String uploadFile(byte[] data);

    /**
     * Download file from S3
     * @param key - key of file
     * @return - file data
     */
    byte[] downloadFile(String key);
}
