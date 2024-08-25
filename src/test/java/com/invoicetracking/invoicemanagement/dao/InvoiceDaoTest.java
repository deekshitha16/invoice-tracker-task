package com.invoicetracking.invoicemanagement.dao;

import com.invoicetracking.invoicemanagement.Enums.InvoiceStatusEnum;
import com.invoicetracking.invoicemanagement.entity.InvoiceEntity;
import com.invoicetracking.invoicemanagement.repository.InvoiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
public class InvoiceDaoTest {

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
    void testSaveInvoice()
    {

    }
}
