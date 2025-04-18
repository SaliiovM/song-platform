package com.microservices.saliiov.resource.resource_service.service.impl;

import com.microservices.saliiov.resource.resource_service.dto.Storage;
import com.microservices.saliiov.resource.resource_service.exception.S3ProcessingException;
import com.microservices.saliiov.resource.resource_service.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectsResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3FileStorageServiceImpl implements FileStorageService {

    private static final String KEY = "%s/%s";
    private final S3Client s3Client;
    @Value("${s3.content-type}")
    private String contentType;

    @Override
    public String uploadFile(byte[] data, Storage storage) {
        try {
            String key = String.format(KEY, storage.getPath(),  UUID.randomUUID());
            s3Client.putObject(PutObjectRequest.builder()
                    .bucket(storage.getBucket())
                    .key(key)
                    .contentType(contentType)
                    .build(), RequestBody.fromBytes(data));
            return key;
        } catch (Exception e) {
            log.error("Failed to upload file to S3", e);
            throw new S3ProcessingException("Failed to upload file to S3");
        }
    }

    @Override
    public byte[] downloadFile(String key, Storage storage) {
        log.info("Downloading file: {}", key);
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(storage.getBucket())
                .key(key)
                .build();

        try (ResponseInputStream<?> responseStream = s3Client.getObject(getObjectRequest)) {
            return responseStream.readAllBytes();
        } catch (NoSuchKeyException e) {
            log.error("The specified file does not exist: {}", key, e);
            throw new S3ProcessingException("The specified file does not exist: " + key);
        } catch (IOException e) {
            log.error("Error while reading the file data from S3", e);
            throw new S3ProcessingException("Error while reading the file data from S3");
        }
    }

    @Override
    public void deleteFiles(List<String> keys, Storage storage) {
        DeleteObjectsRequest deleteObjectsRequest = DeleteObjectsRequest.builder()
                .bucket(storage.getBucket())
                .delete(builder -> builder.objects(keys.stream()
                        .map(key -> ObjectIdentifier.builder().key(key).build())
                        .collect(Collectors.toList())))
                .build();

        try {
            DeleteObjectsResponse deleteObjectsResponse = s3Client.deleteObjects(deleteObjectsRequest);
            log.info("Deleted {} objects.", deleteObjectsResponse.deleted().size());
        } catch (Exception e) {
            log.error("Failed to delete files from S3", e);
            throw new S3ProcessingException("Failed to delete files from S3");
        }
    }

    @Override
    public void deleteFile(String key, Storage storage) {
        try {
            s3Client.deleteObject(builder -> builder.bucket(storage.getBucket()).key(key));
        } catch (Exception e) {
            log.error("Failed to delete file from S3", e);
            throw new S3ProcessingException("Failed to delete file from S3");
        }
    }

    @Override
    public String moveFile(String key, Storage from, Storage to) {
        try {
            byte[] data = downloadFile(key, from);
            String newKey = uploadFile(data, to);
            deleteFile(key, from);
            return newKey;
        } catch (Exception e) {
            log.error("Failed to move file in S3", e);
            throw new S3ProcessingException("Failed to move file in S3");
        }
    }
}
