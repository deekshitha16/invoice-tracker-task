package com.invoicetracking.invoicemanagement.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class InvoiceCreateRequestDTO {

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private Double amount;

    @NotNull(message = "Due date cannot be null")
    @FutureOrPresent(message = "Due date should not be less than Current Date")
    private LocalDate dueDate;
}
