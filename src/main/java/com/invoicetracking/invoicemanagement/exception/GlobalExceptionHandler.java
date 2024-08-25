package com.invoicetracking.invoicemanagement.exception;

import com.invoicetracking.invoicemanagement.dto.BaseResponseDTO;
import com.invoicetracking.invoicemanagement.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponseDTO> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String requiredTypeName = Objects.requireNonNull(ex.getRequiredType()).getSimpleName();
        String errorMessage;
        if (LocalDate.class.isAssignableFrom(ex.getRequiredType())) {
            errorMessage = String.format("Invalid format for field '%s'. Please use the format YYYY-MM-DD.", ex.getName());
        } else {
            errorMessage = String.format("Failed to convert parameter '%s' with value '%s' to type '%s'.", ex.getName(), ex.getValue(), requiredTypeName);
        }

        return ResponseEntity.badRequest().body(BaseResponseDTO.builder().message(errorMessage).statusCode(HttpStatus.BAD_REQUEST.value()).build());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<BaseResponseDTO> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex) {

        String errorMessage = String.format("%s is Mandatory", ex.getParameterName());
        return ResponseEntity.badRequest().body(BaseResponseDTO.builder()
                .message(errorMessage)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build());
    }

    @ExceptionHandler(InvoiceNotFoundException.class)
    public ResponseEntity<BaseResponseDTO> handleInvoiceNotFoundException(InvoiceNotFoundException ex) {

        return ResponseEntity.badRequest().body(BaseResponseDTO.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .build());
    }


}
