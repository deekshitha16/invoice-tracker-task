package com.invoicetracking.invoicemanagement.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseResponseDTO {
    private String message;
    private Integer statusCode;
}
