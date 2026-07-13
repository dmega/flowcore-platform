package com.example.project_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class AccessDeniedException extends ErrorResponseException {

    public AccessDeniedException(String message) {
        super(HttpStatus.FORBIDDEN,
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.FORBIDDEN,
                        message),
                null);
    }
}
