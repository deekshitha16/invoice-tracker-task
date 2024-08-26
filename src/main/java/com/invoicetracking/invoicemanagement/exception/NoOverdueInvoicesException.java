package com.invoicetracking.invoicemanagement.exception;

public class NoOverdueInvoicesException extends RuntimeException {

    public NoOverdueInvoicesException() {

    }

    public NoOverdueInvoicesException(String message) {
        super(message);
    }
}
