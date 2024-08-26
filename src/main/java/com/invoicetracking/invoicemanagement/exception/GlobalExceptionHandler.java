package com.invoicetracking.invoicemanagement.exception;

import com.invoicetracking.invoicemanagement.dto.BaseResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(BaseResponseDTO.builder()
                .message(errorMessage)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponseDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String errorMessage = "Invalid Date format. Use YYYY-MM-DD";
        return ResponseEntity.badRequest().body(BaseResponseDTO.builder()
                .message(errorMessage)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build());
    }

    @ExceptionHandler(InvoiceNotFoundException.class)
    public ResponseEntity<BaseResponseDTO> handleInvoiceNotFoundException(InvoiceNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDTO.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .build());
    }

    @ExceptionHandler(NoOverdueInvoicesException.class)
    public ResponseEntity<BaseResponseDTO> handleNoOverdueInvoicesException(NoOverdueInvoicesException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BaseResponseDTO.builder()
                        .message(ex.getMessage())
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .build());
    }

}
