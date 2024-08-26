package com.invoicetracking.invoicemanagement.dao;

import com.invoicetracking.invoicemanagement.Enums.InvoiceStatusEnum;
import com.invoicetracking.invoicemanagement.entity.InvoiceEntity;
import com.invoicetracking.invoicemanagement.repository.InvoiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class InvoiceDao {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Transactional
    public Long saveInvoice(InvoiceEntity invoiceEntity) {
        return invoiceRepository.save(invoiceEntity).getId();
    }

    public List<InvoiceEntity> getAllInvoices(boolean activeFlag) {
        return invoiceRepository.findAllByIsActive(activeFlag);
    }

    public Optional<InvoiceEntity> getInvoiceById(Long id,boolean activeFlag) {
        return invoiceRepository.findByIdAndIsActive(id,activeFlag);
    }

    public List<InvoiceEntity> getAllOverdueInvoices(LocalDate currentDate, InvoiceStatusEnum status, boolean activeFlag) {
        return invoiceRepository.findByDueDateBeforeAndStatusAndIsActive(currentDate, status, activeFlag);
    }

    @Transactional
    public void processOverDueInvoice(List<InvoiceEntity> newOverDueInvoice, List<InvoiceEntity> updateExistingInvoiceStatus) {
        invoiceRepository.saveAll(newOverDueInvoice);
        invoiceRepository.saveAll(updateExistingInvoiceStatus);
    }

}
