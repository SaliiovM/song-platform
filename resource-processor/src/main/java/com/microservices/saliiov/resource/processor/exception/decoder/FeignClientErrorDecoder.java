package com.microservices.saliiov.resource.processor.exception.decoder;

import com.microservices.saliiov.resource.processor.exception.ClientRetryException;
import com.microservices.saliiov.resource.processor.exception.ResourceAlreadyExistsException;
import com.microservices.saliiov.resource.processor.exception.ResourceNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class FeignClientErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 400) {
            if (response.status() == HttpStatus.NOT_FOUND.value()) {
                return new ResourceNotFoundException("Resource not found");
            } else if (response.status() == HttpStatus.CONFLICT.value()) {
                return new ResourceAlreadyExistsException("Resource already exists");
            }
            return new ClientRetryException("Error occurred while calling the service: " + response.status());
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }

}
