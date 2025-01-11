package com.stergion.githubbackend.domain.contirbutions.fetch;

public class FetchException extends RuntimeException {
    public FetchException(String message) {
        super(message);
    }

    public FetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
