package com.microservices.saliiov.resource.resource_service.service.impl;

import com.microservices.saliiov.resource.resource_service.exception.S3ProcessingException;
import com.microservices.saliiov.resource.resource_service.service.ResourceFileStorageService;
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
public class S3ResourceFileStorageServiceImpl implements ResourceFileStorageService {

    private final S3Client s3Client;
    @Value("${s3.bucket-name}")
    private String bucketName;
    @Value("${s3.content-type}")
    private String contentType;

    @Override
    public String uploadFile(byte[] data) {
        try {
            String fileName = UUID.randomUUID().toString();
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(contentType)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(data));
            return fileName;
        } catch (Exception e) {
            log.error("Failed to upload file to S3", e);
            throw new S3ProcessingException("Failed to upload file to S3");
        }
    }

    @Override
    public byte[] downloadFile(String fileName) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        try (ResponseInputStream<?> responseStream = s3Client.getObject(getObjectRequest)) {
            return responseStream.readAllBytes();
        } catch (NoSuchKeyException e) {
            log.error("The specified file does not exist: {}", fileName, e);
            throw new S3ProcessingException("The specified file does not exist: " + fileName);
        } catch (IOException e) {
            log.error("Error while reading the file data from S3", e);
            throw new S3ProcessingException("Error while reading the file data from S3");
        }
    }

    @Override
    public void deleteFiles(List<String> keys) {
        List<ObjectIdentifier> objectsToDelete = keys.stream()
                .map(key -> ObjectIdentifier.builder().key(key).build())
                .collect(Collectors.toList());

        DeleteObjectsRequest deleteObjectsRequest = DeleteObjectsRequest.builder()
                .bucket(bucketName)
                .delete(builder -> builder.objects(objectsToDelete))
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
    public void deleteFile(String key) {
        try {
            s3Client.deleteObject(builder -> builder.bucket(bucketName).key(key));
        } catch (Exception e) {
            log.error("Failed to delete file from S3", e);
            throw new S3ProcessingException("Failed to delete file from S3");
        }
    }
}
