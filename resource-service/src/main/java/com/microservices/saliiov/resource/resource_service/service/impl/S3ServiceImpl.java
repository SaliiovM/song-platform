package com.microservices.saliiov.resource.resource_service.service.impl;

import com.microservices.saliiov.resource.resource_service.exception.S3ProcessingException;
import com.microservices.saliiov.resource.resource_service.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

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
}
