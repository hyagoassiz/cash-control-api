package com.cashcontrol.config;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.cashcontrol.config.dto.ApiErrorResponse;
import com.cashcontrol.config.dto.ApiFieldError;
import com.cashcontrol.exception.EmailAlreadyExistsException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        List<ApiFieldError> errors = new ArrayList<>();
        errors.add(new ApiFieldError("email", "EMAIL_ALREADY_EXISTS", ex.getMessage()));

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.CONFLICT.value(),
                "EMAIL_ALREADY_EXISTS",
                ex.getMessage(),
                errors
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ApiFieldError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(this::fieldErrorToApiFieldError)
                .collect(Collectors.toList());

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                "Dados de usuário inválidos",
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        List<ApiFieldError> errors = new ArrayList<>();
        errors.add(new ApiFieldError("general", "ILLEGAL_ARGUMENT", ex.getMessage()));

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "ILLEGAL_ARGUMENT",
                ex.getMessage(),
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    private ApiFieldError fieldErrorToApiFieldError(FieldError fieldError) {
        String code = extractErrorCode(fieldError);
        return new ApiFieldError(
                fieldError.getField(),
                code,
                fieldError.getDefaultMessage()
        );
    }

    private String extractErrorCode(FieldError fieldError) {
        String annotation = fieldError.getCode();
        
        if (annotation == null || annotation.isEmpty()) {
            return "VALIDATION_ERROR";
        }

        return switch (annotation) {
            case "NotBlank" -> "FIELD_REQUIRED";
            case "Email" -> "EMAIL_INVALID";
            case "Pattern" -> "PATTERN_INVALID";
            case "Size" -> "SIZE_INVALID";
            case "NotNull" -> "FIELD_REQUIRED";
            case "Min" -> "MIN_VALUE_INVALID";
            case "Max" -> "MAX_VALUE_INVALID";
            default -> annotation;
        };
    }
}
