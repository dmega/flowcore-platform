package com.example.project_backend.controller;

import com.example.project_backend.exception.AccessDeniedException;
import com.example.project_backend.exception.BadRequestException;
import com.example.project_backend.exception.InvalidCredentialsException;
import com.example.project_backend.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public final ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.info("MethodArgumentNotValidException", ex);
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(problemDetail);
    }

    @ExceptionHandler({BadRequestException.class})
    public final ResponseEntity<ProblemDetail> handleBadRequestException(BadRequestException ex) {
        log.info("BadRequestException", ex);
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(problemDetail);
    }

    @ExceptionHandler({InvalidCredentialsException.class})
    public final ResponseEntity<ProblemDetail> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        log.info("InvalidCredentialsException", ex);
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(problemDetail);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public final ResponseEntity<ProblemDetail> handleAccessDeniedException(AccessDeniedException ex) {
        log.info("AccessDeniedException", ex);
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body(problemDetail);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public final ResponseEntity<ProblemDetail> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.debug("ResourceNotFoundException", ex);
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler({Exception.class})
    public final ResponseEntity<ProblemDetail> handleException(Exception ex) {
        log.warn("Controller server exception", ex);
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return ResponseEntity.internalServerError().body(problemDetail);
    }

}
