package com.invoicetracking.invoicemanagement.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class InvoiceResponseDTO {

    private Long invoiceId;

    private Double amount;

    private Double paidAmount;

    private LocalDate dueDate;

    private String status;
}
