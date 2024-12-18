package com.stergion.githubbackend.external.githubservice.client.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.external.githubservice.utils.ErrorResponseMapper;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

@Provider
public class ServiceResponseExceptionMapper
        implements ResponseExceptionMapper<ServiceResponseException> {

    private final ErrorResponseMapper errorResponseMapper;


    public ServiceResponseExceptionMapper(ObjectMapper objectMapper) {
        this.errorResponseMapper = new ErrorResponseMapper(objectMapper.copy());
    }

    @Override
    public ServiceResponseException toThrowable(Response response) {
        String body = response.readEntity(String.class);
        return errorResponseMapper.toThrowable(body);
    }
}
