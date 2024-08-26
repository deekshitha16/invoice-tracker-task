package com.invoicetracking.invoicemanagement.controller;

import com.invoicetracking.invoicemanagement.dto.InvoiceCreateRequestDTO;
import com.invoicetracking.invoicemanagement.dto.InvoiceOverduePaymentRequestDTO;
import com.invoicetracking.invoicemanagement.dto.InvoicePaymentRequestDTO;
import com.invoicetracking.invoicemanagement.dto.ResponseDTO;
import com.invoicetracking.invoicemanagement.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<ResponseDTO> createInvoice(@Valid @RequestBody InvoiceCreateRequestDTO invoiceCreateRequestDTO) {
        return new ResponseEntity<>(invoiceService.createInvoice(invoiceCreateRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> getAllInvoices() {
        return new ResponseEntity<>(invoiceService.getAllInvoices(), HttpStatus.OK);
    }

    @PostMapping("/{invoiceId}/payments")
    public ResponseEntity<ResponseDTO> payInvoice(@PathVariable Long invoiceId, @Valid @RequestBody InvoicePaymentRequestDTO invoicePaymentRequestDTO) {
        return new ResponseEntity<>(invoiceService.payInvoice(invoiceId, invoicePaymentRequestDTO), HttpStatus.OK);
    }

    @PostMapping("/process-overdue")
    public ResponseEntity<ResponseDTO> processOverduePayment(@Valid @RequestBody InvoiceOverduePaymentRequestDTO invoiceOverduePaymentRequestDTO) {
        return new ResponseEntity<>(invoiceService.processOverduePayment(invoiceOverduePaymentRequestDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{invoiceId}")
    public ResponseEntity<ResponseDTO> deleteInvoice(@PathVariable Long invoiceId) {
        return new ResponseEntity<>(invoiceService.deleteInvoice(invoiceId), HttpStatus.OK);
    }


}
