package com.invoicetracking.invoicemanagement.Enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InvoiceStatusEnum {

    PENDING("pending"),
    PAID("paid"),
    VOID("void");

    private final String value;
}
