package com.microservices.saliiov.resource.resource_service.service;

import java.util.List;

/**
 * Service for file storage
 */
public interface FileStorageService {

    /**
     * Upload file
     * @param data - file data
     * @return - key of uploaded file
     */
    String uploadFile(byte[] data);

    /**
     * Download file
     * @param key - key of file
     * @return - file data
     */
    byte[] downloadFile(String key);

    /**
     * Delete files
     * @param keys - keys of files
     */
    void deleteFiles(List<String> keys);

    /**
     * Delete file
     * @param key - key of file
     */
    void deleteFile(String key);
}
