package com.invoicetracking.invoicemanagement.dao;

import com.invoicetracking.invoicemanagement.Enums.InvoiceStatusEnum;
import com.invoicetracking.invoicemanagement.constant.MessageConstant;
import com.invoicetracking.invoicemanagement.entity.InvoiceEntity;
import com.invoicetracking.invoicemanagement.repository.InvoiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class InvoiceDaoTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoiceDao invoiceDao;

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
    void testSaveInvoice() {
        InvoiceEntity invoiceEntity = createTestInvoice(1L, 200.0, 0.0, InvoiceStatusEnum.PENDING, LocalDate.now().plusDays(10));
        when(invoiceRepository.save(invoiceEntity)).thenReturn(invoiceEntity);
        Long savedInvoiceId = invoiceDao.saveInvoice(invoiceEntity);
        assertNotNull(savedInvoiceId);
        assertEquals(1L, savedInvoiceId);
    }

    @Test
    void testGetAllInvoices() {
        InvoiceEntity invoiceEntity = createTestInvoice(1L, 200.0, 0.0, InvoiceStatusEnum.PENDING, LocalDate.now().plusDays(10));
        when(invoiceRepository.findAllByIsActive(MessageConstant.ACTIVE_FLAG)).thenReturn(List.of(invoiceEntity));
        List<InvoiceEntity> result = invoiceDao.getAllInvoices(true);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetInvoiceById() {
        InvoiceEntity invoiceEntity = createTestInvoice(1L, 200.0, 0.0, InvoiceStatusEnum.PENDING, LocalDate.now().plusDays(10));
        when(invoiceRepository.findByIdAndIsActive(1L,MessageConstant.ACTIVE_FLAG)).thenReturn(Optional.of(invoiceEntity));
        Optional<InvoiceEntity> result = invoiceDao.getInvoiceById(1L, true);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testGetAllOverdueInvoices() {
        InvoiceEntity invoiceEntity = createTestInvoice(1L, 200.0, 0.0, InvoiceStatusEnum.PENDING, LocalDate.now().minusDays(10));
        when(invoiceRepository.findByDueDateBeforeAndStatusAndIsActive(LocalDate.now(), InvoiceStatusEnum.PENDING, MessageConstant.ACTIVE_FLAG))
                .thenReturn(List.of(invoiceEntity));
        List<InvoiceEntity> result = invoiceDao.getAllOverdueInvoices(LocalDate.now(), InvoiceStatusEnum.PENDING, MessageConstant.ACTIVE_FLAG);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testProcessOverDueInvoice() {
        InvoiceEntity invoiceEntity1 = createTestInvoice(1L, 200.0, 0.0, InvoiceStatusEnum.PENDING, LocalDate.now().plusDays(10));
        InvoiceEntity invoiceEntity2 = createTestInvoice(1L, 300.0, 0.0, InvoiceStatusEnum.PAID, LocalDate.now().plusDays(10));
        List<InvoiceEntity> newOverDueInvoice = List.of(invoiceEntity1);
        List<InvoiceEntity> updateExistingInvoiceStatus = List.of(invoiceEntity2);
        invoiceDao.processOverDueInvoice(List.of(invoiceEntity1), List.of(invoiceEntity2));

        verify(invoiceRepository, times(1)).saveAll(newOverDueInvoice);
        verify(invoiceRepository, times(1)).saveAll(updateExistingInvoiceStatus);
    }
}
