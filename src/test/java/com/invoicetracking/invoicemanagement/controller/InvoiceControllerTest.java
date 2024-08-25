package com.invoicetracking.invoicemanagement.controller;

import com.invoicetracking.invoicemanagement.Enums.InvoiceStatusEnum;
import com.invoicetracking.invoicemanagement.constant.MessageConstant;
import com.invoicetracking.invoicemanagement.dto.InvoiceCreateRequestDTO;
import com.invoicetracking.invoicemanagement.dto.InvoiceOverduePaymentRequestDTO;
import com.invoicetracking.invoicemanagement.dto.InvoicePaymentRequestDTO;
import com.invoicetracking.invoicemanagement.dto.InvoiceResponseDTO;
import com.invoicetracking.invoicemanagement.dto.ResponseDTO;
import com.invoicetracking.invoicemanagement.service.InvoiceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class InvoiceControllerTest {

    @Mock
    private InvoiceService invoiceService;

    @InjectMocks
    private InvoiceController invoiceController;

    @Test
    void testCreateInvoice() {
        InvoiceCreateRequestDTO invoiceCreateRequestDTO = new InvoiceCreateRequestDTO();
        invoiceCreateRequestDTO.setAmount(100.0);
        invoiceCreateRequestDTO.setDueDate(LocalDate.now().plusDays(10));
        ResponseDTO response = ResponseDTO.builder().message(MessageConstant.INVOICE_CREATED).build();
        when(invoiceService.createInvoice(any())).thenReturn(response);
        ResponseEntity<ResponseDTO> responseEntity = invoiceController.createInvoice(invoiceCreateRequestDTO);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void testGetAllInvoices() {
        InvoiceResponseDTO invoiceResponseDTO = InvoiceResponseDTO.builder().invoiceId(1L).paidAmount(100.0).amount(200.0).
                dueDate(LocalDate.now().plusDays(10)).status(String.valueOf(InvoiceStatusEnum.PENDING)).build();
        ResponseDTO response = ResponseDTO.builder().message(MessageConstant.INVOICE_LIST_FETCHED).
                data(List.of(invoiceResponseDTO)).build();
        when(invoiceService.getAllInvoices()).thenReturn(response);
        ResponseEntity<ResponseDTO> responseEntity = invoiceController.getAllInvoices();
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testPayInvoice() {
        InvoicePaymentRequestDTO invoicePaymentRequestDTO = new InvoicePaymentRequestDTO();
        invoicePaymentRequestDTO.setAmount(100.0);
        Long invoiceId = 1L;
        ResponseDTO response = ResponseDTO.builder().message(MessageConstant.PAYMENT_SUCCESS).build();
        when(invoiceService.createInvoice(any())).thenReturn(response);
        ResponseEntity<ResponseDTO> responseEntity = invoiceController.payInvoice(invoiceId, invoicePaymentRequestDTO);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testProcessOverduePayment() {
        InvoiceOverduePaymentRequestDTO overduePaymentRequestDTO = new InvoiceOverduePaymentRequestDTO();
        overduePaymentRequestDTO.setLateFee(10.0);
        overduePaymentRequestDTO.setOverdueDays(10);
        ResponseDTO response = ResponseDTO.builder().message(MessageConstant.OVERDUE_INVOICE_UPDATED).build();
        when(invoiceService.processOverduePayment(overduePaymentRequestDTO)).thenReturn(response);
        ResponseEntity<ResponseDTO> responseEntity = invoiceController.processOverduePayment(overduePaymentRequestDTO);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
