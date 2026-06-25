package com.linklingua.common;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Global exception handler.
 *
 * <p>Intercepts exceptions thrown by controllers and converts them into the standard
 * {@link Result} structure, preventing raw stack traces from being exposed to the frontend.</p>
 *
 * @author LinkLingua
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles custom business exceptions.
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn("Business exception: {}", e.getMessage());
        return Result.failed(e.getCode(), e.getMessage());
    }

    /**
     * Handles validation errors on {@code @RequestBody} arguments (failed {@code @Valid} object).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn("Parameter validation failed: {}", message);
        return Result.failed(ResultCode.VALIDATE_FAILED.getCode(), message);
    }

    /**
     * Handles binding/validation errors on form objects.
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleBindException(BindException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn("Parameter binding failed: {}", message);
        return Result.failed(ResultCode.VALIDATE_FAILED.getCode(), message);
    }

    /**
     * Handles validation errors on method parameters (e.g. {@code @RequestParam}, {@code @PathVariable}).
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
        log.warn("Constraint validation failed: {}", message);
        return Result.failed(ResultCode.VALIDATE_FAILED.getCode(), message);
    }

    /**
     * Handles unreadable or malformed request bodies (e.g. invalid JSON or a date that does
     * not match the expected format). Returns a clean 400-style result instead of a 500.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("Malformed request body: {}", e.getMostSpecificCause().getMessage());
        return Result.failed(ResultCode.VALIDATE_FAILED.getCode(),
                "Malformed or unreadable request body");
    }

    /**
     * Fallback handler for all uncaught exceptions.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e) {
        log.error("System exception: ", e);
        return Result.failed(ResultCode.FAILED.getCode(), "System error, please contact the administrator");
    }
}
