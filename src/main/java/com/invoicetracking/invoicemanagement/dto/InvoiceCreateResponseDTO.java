package com.invoicetracking.invoicemanagement.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class InvoiceCreateResponseDTO extends ResponseDTO{

    private Long invoiceId;
}
