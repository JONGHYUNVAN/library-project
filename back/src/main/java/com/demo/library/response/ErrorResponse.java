package com.demo.library.response;

import com.demo.library.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

    private int status;
    private String message;
    private List<FieldError> fieldErrors;
    private List<ConstraintViolationError> constraintViolationErrors;

    public static ErrorResponse of(HttpStatus httpStatus) {
        return new ErrorResponse(httpStatus.value(), httpStatus.getReasonPhrase(), null, null);
    }

    public static ErrorResponse of(HttpStatus httpStatus, String message) {
        return new ErrorResponse(httpStatus.value(), message, null, null);
    }

    public static ErrorResponse of(ExceptionCode exceptionCode) {
        return new ErrorResponse(exceptionCode.getStatus(), exceptionCode.getMessage(), null, null);
    }

    public static ErrorResponse of(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors().stream()
                .map(error -> new FieldError(error.getField(), error.getRejectedValue(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                fieldErrors, null);
    }

    public static ErrorResponse of(Set<ConstraintViolation<?>> violations) {
        List<ConstraintViolationError> constraintViolationErrors = violations.stream()
                .map(violation -> new ConstraintViolationError(
                        violation.getPropertyPath().toString(),
                        violation.getInvalidValue(),
                        violation.getMessage()))
                .collect(Collectors.toList());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                null, constraintViolationErrors);
    }

    @Getter
    @AllArgsConstructor
    public static class FieldError {
        private String field;
        private Object rejectValue;
        private String reason;
    }

    @Getter
    @AllArgsConstructor
    public static class ConstraintViolationError {
        private String propertyPath;
        private Object rejectValue;
        private String reason;
    }
}