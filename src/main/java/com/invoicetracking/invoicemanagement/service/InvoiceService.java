package com.invoicetracking.invoicemanagement.service;

import com.invoicetracking.invoicemanagement.dto.InvoiceCreateRequestDTO;
import com.invoicetracking.invoicemanagement.dto.InvoiceOverduePaymentRequestDTO;
import com.invoicetracking.invoicemanagement.dto.InvoicePaymentRequestDTO;
import com.invoicetracking.invoicemanagement.dto.ResponseDTO;

public interface InvoiceService {


    ResponseDTO createInvoice(InvoiceCreateRequestDTO invoiceCreateRequestDTO);

    ResponseDTO getAllInvoices();

    ResponseDTO payInvoice(Long id, InvoicePaymentRequestDTO invoicePaymentRequestDTO);

    ResponseDTO processOverduePayment(InvoiceOverduePaymentRequestDTO invoiceOverduePaymentRequestDTO);
}
