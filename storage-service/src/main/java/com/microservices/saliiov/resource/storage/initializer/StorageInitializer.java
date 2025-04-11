package com.microservices.saliiov.resource.storage.initializer;

import com.microservices.saliiov.resource.storage.config.StorageProperties;
import com.microservices.saliiov.resource.storage.entity.Storage;
import com.microservices.saliiov.resource.storage.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StorageInitializer implements CommandLineRunner {

    private final StorageRepository storageRepository;
    private final StorageProperties storageProperties;

    @Override
    public void run(String... args) {
        log.info("Initializing storage configurations...");
        List<StorageProperties.StorageConfig> storageConfigs = storageProperties.getTypes();
        for (StorageProperties.StorageConfig config : storageConfigs) {
            if (storageRepository.findByTypeAndBucket(config.getStorageType(), config.getBucket()).isEmpty()) {
                log.info("Creating storage configuration: type={}, bucket={}, path={}", config.getStorageType(), config.getBucket(), config.getPath());
                Storage storage = new Storage();
                storage.setType(config.getStorageType());
                storage.setBucket(config.getBucket());
                storage.setPath(config.getPath());
                storageRepository.save(storage);
            }
        }
    }
}
