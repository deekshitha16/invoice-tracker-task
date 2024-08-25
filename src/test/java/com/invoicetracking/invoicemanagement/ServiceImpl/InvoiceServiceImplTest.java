package com.invoicetracking.invoicemanagement.ServiceImpl;

import com.invoicetracking.invoicemanagement.Enums.InvoiceStatusEnum;
import com.invoicetracking.invoicemanagement.constant.MessageConstant;
import com.invoicetracking.invoicemanagement.dao.InvoiceDao;
import com.invoicetracking.invoicemanagement.dto.InvoiceCreateRequestDTO;
import com.invoicetracking.invoicemanagement.dto.InvoiceCreateResponseDTO;
import com.invoicetracking.invoicemanagement.dto.InvoiceOverduePaymentRequestDTO;
import com.invoicetracking.invoicemanagement.dto.InvoicePaymentRequestDTO;
import com.invoicetracking.invoicemanagement.dto.InvoiceResponseDTO;
import com.invoicetracking.invoicemanagement.dto.ResponseDTO;
import com.invoicetracking.invoicemanagement.entity.InvoiceEntity;
import com.invoicetracking.invoicemanagement.exception.InvoiceNotFoundException;
import com.invoicetracking.invoicemanagement.service.InvoiceServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class InvoiceServiceImplTest {

    @Mock
    private InvoiceDao invoiceDao;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    private InvoiceEntity createTestInvoice(Long id, double amount, double paidAmount, InvoiceStatusEnum status, LocalDate dueDate) {
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setId(id);
        invoice.setAmount(amount);
        invoice.setPaidAmount(paidAmount);
        invoice.setStatus(status);
        invoice.setDueDate(dueDate);
        return invoice;
    }

    @Test
    void testCreateInvoice() {
        InvoiceCreateRequestDTO requestDTO = new InvoiceCreateRequestDTO();
        requestDTO.setDueDate(LocalDate.now().plusDays(20));
        requestDTO.setAmount(200.0);

        InvoiceEntity invoiceEntity = createTestInvoice(1L, 200.0, 0.0, InvoiceStatusEnum.PENDING, requestDTO.getDueDate());
        when(invoiceDao.saveInvoice(any())).thenReturn(invoiceEntity.getId());
        ResponseDTO responseDTO = invoiceService.createInvoice(requestDTO);
        assertNotNull(responseDTO);
        assertEquals(1L, ((InvoiceCreateResponseDTO) responseDTO.getData()).getInvoiceId());
    }

    @Test
    void testGetAllInvoices() {
        InvoiceEntity invoiceEntity = createTestInvoice(1L, 200.0, 0.0, InvoiceStatusEnum.PENDING, LocalDate.now().plusDays(20));
        when(invoiceDao.getAllInvoices()).thenReturn(List.of(invoiceEntity));
        ResponseDTO responseDTO = invoiceService.getAllInvoices();
        assertNotNull(responseDTO);
        assertEquals(invoiceEntity.getId(), ((InvoiceResponseDTO) responseDTO.getData()).getInvoiceId());
    }

    @Test
    void testPayInvoice() {
        Long invoiceId = 1L;
        InvoicePaymentRequestDTO paymentRequestDTO = new InvoicePaymentRequestDTO();
        paymentRequestDTO.setAmount(50.0);
        when(invoiceDao.getInvoiceById(invoiceId)).thenReturn(Optional.empty());
        assertThrows(InvoiceNotFoundException.class, () -> invoiceService.payInvoice(invoiceId, paymentRequestDTO));

        InvoiceEntity invoiceEntity = createTestInvoice(1L, 200.0, 0.0, InvoiceStatusEnum.PENDING, LocalDate.now().plusDays(20));
        when(invoiceDao.getInvoiceById(invoiceId)).thenReturn(Optional.of(invoiceEntity));
        when(invoiceDao.saveInvoice(invoiceEntity)).thenReturn(invoiceId);
        ResponseDTO responseDTO = invoiceService.payInvoice(invoiceId, paymentRequestDTO);
        assertNotNull(responseDTO);
        assertEquals(1L, ((InvoiceCreateResponseDTO) responseDTO.getData()).getInvoiceId());
    }

    @Test
    void testProcessOverduePayment() {
        InvoiceOverduePaymentRequestDTO overduePaymentRequest = new InvoiceOverduePaymentRequestDTO();
        overduePaymentRequest.setOverdueDays(10);
        overduePaymentRequest.setLateFee(10.0);
        InvoiceEntity unpaidInvoiceEntity = createTestInvoice(1L, 200.0, 0.0, InvoiceStatusEnum.PENDING, LocalDate.now().minusDays(10));
        InvoiceEntity partiallyPaidInvoiceEntity = createTestInvoice(2L, 200.0, 100.0, InvoiceStatusEnum.PENDING, LocalDate.now().minusDays(10));
        List<InvoiceEntity> overdueInvoices = List.of(unpaidInvoiceEntity, partiallyPaidInvoiceEntity);
        when(invoiceDao.getAllOverdueInvoices(any(), any()))
                .thenReturn(overdueInvoices);

        ResponseDTO responseDTO = invoiceService.processOverduePayment(overduePaymentRequest);
        assertNotNull(responseDTO);
        assertEquals(MessageConstant.OVERDUE_INVOICE_UPDATED, responseDTO.getMessage());

    }

}
