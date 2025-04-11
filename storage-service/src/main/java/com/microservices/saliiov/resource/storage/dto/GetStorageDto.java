package com.microservices.saliiov.resource.storage.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GetStorageDto extends CreateStorageDto {
    private Long id;
}
