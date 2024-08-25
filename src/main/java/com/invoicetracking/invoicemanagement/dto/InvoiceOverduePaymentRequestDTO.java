package com.invoicetracking.invoicemanagement.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceOverduePaymentRequestDTO {

    @NotNull(message = "Late Fee cannot be null")
    @DecimalMin(value = "0.0", message = "Late Fee should not be Negative")
    private Double lateFee;

    @NotNull(message = "Over Due Days cannot be null")
    @Min(value = 1, message = "Overdue days must be at least 1")
    private Integer overdueDays;
}
