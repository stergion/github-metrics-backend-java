package com.stergion.githubbackend.application.mapper;

import com.stergion.githubbackend.domain.repositories.RepositoryNotFoundException;
import com.stergion.githubbackend.domain.users.UserAlreadyExistsException;
import com.stergion.githubbackend.domain.users.UserNotFoundException;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

public class ExceptionMapper {
    private static final Logger log = Logger.getLogger(ExceptionMapper.class);

    @ServerExceptionMapper
    public RestResponse<String> mapException(UserNotFoundException e) {
        return RestResponse.status(Response.Status.NOT_FOUND, e.getMessage());
    }

    @ServerExceptionMapper
    public RestResponse<String> mapException(UserAlreadyExistsException e) {
        return RestResponse.status(Response.Status.CONFLICT, e.getMessage());
    }

    @ServerExceptionMapper
    public RestResponse<String> mapException(RepositoryNotFoundException e) {
        return RestResponse.status(Response.Status.NOT_FOUND, e.getMessage());
    }
}
