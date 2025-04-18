package com.microservices.saliiov.resource.resource_service.service;

import com.microservices.saliiov.resource.resource_service.dto.Storage;

import java.util.List;

/**
 * Service for file storage
 */
public interface FileStorageService {

    /**
     * Upload file
     * @param data - file data
     * @param storage - storage
     * @return - key of uploaded file
     */
    String uploadFile(byte[] data, Storage storage);

    /**
     * Download file
     * @param key - key of file
     * @param storage - storage
     * @return - file data
     */
    byte[] downloadFile(String key, Storage storage);

    /**
     * Delete files
     * @param keys - keys of files
     * @param storage - storage
     */
    void deleteFiles(List<String> keys, Storage storage);

    /**
     * Delete file
     * @param key - key of file
     * @param storage - storage
     */
    void deleteFile(String key, Storage storage);

    /**
     * Move file
     * @param key - key of file
     * @param from - source storage
     * @param to - destination storage
     * @return - new key of moved file
     */
    String moveFile(String key, Storage from, Storage to);
}
