package com.microservices.saliiov.resource.storage.service.impl;

import com.microservices.saliiov.resource.storage.dto.CreateStorageDto;
import com.microservices.saliiov.resource.storage.dto.GetStorageDto;
import com.microservices.saliiov.resource.storage.entity.Storage;
import com.microservices.saliiov.resource.storage.exception.StorageNotFoundException;
import com.microservices.saliiov.resource.storage.exception.StorageValidationException;
import com.microservices.saliiov.resource.storage.repository.StorageRepository;
import com.microservices.saliiov.resource.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final StorageRepository repository;

    @Override
    public Long createStorage(CreateStorageDto dto) {
        validateStorageDto(dto);
        return repository.save(mapToStorageMetaData(dto)).getId();
    }

    @Override
    public List<GetStorageDto> getAllStorages() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public void deleteStorages(String ids) {
        validate(ids);
        List<Long> idsToDelete = Arrays.stream(StringUtils.split(ids, ",")).map(Long::valueOf).toList();
        repository.deleteAllById(idsToDelete);
    }

    @Override
    public GetStorageDto getStorageByType(String type) {
        return Optional.ofNullable(type)
                .filter(StringUtils::isNotBlank)
                .map(repository::findByType)
                .orElseThrow(() -> new StorageValidationException("Storage type must be provided"))
                .map(this::mapToDto)
                .orElseThrow(() -> new StorageNotFoundException("Storage with type '" + type + "' not found"));
    }

    private void validateStorageDto(CreateStorageDto dto) {
        if (Objects.isNull(dto) || StringUtils.isAnyBlank(dto.getStorageType(), dto.getPath(), dto.getBucket())) {
            throw new StorageValidationException("Storage type, path and bucket must be provided");
        }

        if (repository.findByTypeAndBucket(dto.getStorageType(), dto.getBucket()).isPresent()) {
            throw new StorageValidationException("Storage with the same type and bucket already exists");
        }
    }

    private Storage mapToStorageMetaData(CreateStorageDto dto) {
        Storage storage = new Storage();
        storage.setType(dto.getStorageType());
        storage.setPath(dto.getPath());
        storage.setBucket(dto.getBucket());
        return storage;
    }

    private GetStorageDto mapToDto(Storage entity) {
        GetStorageDto dto = new GetStorageDto();
        dto.setId(entity.getId());
        dto.setBucket(entity.getBucket());
        dto.setPath(entity.getPath());
        dto.setStorageType(entity.getType());
        return dto;
    }

    private void validate(String ids) {
        if (StringUtils.length(ids) > 200) {
            throw new StorageValidationException("ids are too long");
        }

        if (Arrays.stream(StringUtils.split(ids, ",")).anyMatch(id -> !NumberUtils.isDigits(id))) {
            throw new StorageValidationException("ids are required and must be numbers");
        }
    }
}
