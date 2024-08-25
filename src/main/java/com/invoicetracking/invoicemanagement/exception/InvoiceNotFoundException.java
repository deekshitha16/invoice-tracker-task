package com.invoicetracking.invoicemanagement.exception;

public class InvoiceNotFoundException extends RuntimeException {
    public InvoiceNotFoundException() {
    }

    public InvoiceNotFoundException(String message) {
        super(message);
    }
}
