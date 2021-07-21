package com.yataygecisle.preference.profiles.web.exceptions;

import com.yataygecisle.commons.models.ErrorResponse;
import com.yataygecisle.preference.profiles.web.model.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.warn("HttpMessageNotReadableException: {}", ex.getMessage());
        return errorResponse(new ErrorResponse(ErrorType.INVALID_REQUEST.getErr(), ex.getMessage(),  HttpStatus.BAD_REQUEST));
    }

    @Override
    protected ResponseEntity handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.warn("NoHandlerFoundException: {}", ex.getMessage());
        return errorResponse(new ErrorResponse(ErrorType.INVALID_REQUEST.getErr(), ex.getMessage(),  HttpStatus.NOT_FOUND));
    }

    @Override
    protected ResponseEntity handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.warn("HttpMessageNotWritableException: {}", ex.getMessage());
        return errorResponse(new ErrorResponse(ErrorType.INVALID_REQUEST.getErr(), ex.getMessage(),  HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Override
    protected ResponseEntity handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.warn("HttpMediaTypeNotAcceptableException: {}", ex.getMessage());
        return errorResponse(new ErrorResponse(ErrorType.INVALID_REQUEST.getErr(), ex.getMessage(),  HttpStatus.BAD_REQUEST));
    }

    @Override
    protected ResponseEntity handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.warn("HttpMediaTypeNotSupportedException: {}", ex.getMessage());
        return errorResponse(new ErrorResponse(ErrorType.INVALID_REQUEST.getErr(), ex.getMessage(),  HttpStatus.UNSUPPORTED_MEDIA_TYPE));
    }

    @Override
    protected ResponseEntity handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.warn("MissingServletRequestParameterException: {}", ex.getMessage());
        return errorResponse(new ErrorResponse(ErrorType.INVALID_REQUEST.getErr(), ex.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @Override
    protected ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.warn("MethodArgumentNotValidException: {}", ex.getMessage());
        return errorResponse(new ErrorResponse(ErrorType.INVALID_REQUEST.getErr(), ex.getFieldErrors(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        log.warn("ConstraintViolationException: {}", ex.getMessage());
        return errorResponse(new ErrorResponse(ErrorType.INVALID_REQUEST.getErr(), ex.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("IllegalArgumentException: {}", ex.getMessage());
        return errorResponse(new ErrorResponse(ErrorType.INVALID_REQUEST.getErr(), ex.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex) {
        log.warn("NullPointerException: {} {} {} {}", ex.getStackTrace(), ex.getSuppressed(), ex.getCause(), ex.getMessage());
        return errorResponse(new ErrorResponse(ErrorType.SERVER_ERROR.getErr(), ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(StudentNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleStudentNotFoundException(StudentNotFoundException ex) {
        log.warn("StudentNotFoundException: {} {}", ex.getLocalizedMessage(), ex.getMessage());
        return errorResponse(new ErrorResponse(ErrorType.NOT_FOUND.getErr(), ex.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(CourseNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleBasketItemNotFoundException(CourseNotFoundException ex) {
        log.warn("CourseNotFoundException: {} {}", ex.getLocalizedMessage(), ex.getMessage());
        return errorResponse(new ErrorResponse(ErrorType.NOT_FOUND.getErr(), ex.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(InvalidInteractionTypeException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidInteractionTypeException(InvalidInteractionTypeException ex) {
        log.warn("InvalidInteractionTypeException: {} {}", ex.getLocalizedMessage(), ex.getMessage());
        return errorResponse(new ErrorResponse(ErrorType.INVALID_REQUEST.getErr(), ex.getMessage(), HttpStatus.BAD_REQUEST));
    }

    private ResponseEntity<ErrorResponse> errorResponse(ErrorResponse errorResponse){
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }


}
