package com.invoicetracking.invoicemanagement.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    InvoiceDao invoiceDao;

    @Override
    public ResponseDTO createInvoice(InvoiceCreateRequestDTO invoiceCreateRequestDTO) {
        InvoiceEntity saveInvoice = saveInvoiceDetails(invoiceCreateRequestDTO.getDueDate(), invoiceCreateRequestDTO.getAmount());
        return ResponseDTO.builder().data(InvoiceCreateResponseDTO.builder().invoiceId(
                        processInvoice(saveInvoice)).build()).
                message(MessageConstant.INVOICE_CREATED).build();
    }

    private Long processInvoice(InvoiceEntity invoice) {
        return invoiceDao.saveInvoice(invoice);
    }

    @Override
    public ResponseDTO getAllInvoices() {
        List<InvoiceResponseDTO> invoiceList = invoiceDao.getAllInvoices()
                .stream().map(this::mapEntityToResponseDTO).toList();
        return ResponseDTO.builder().data(invoiceList).
                message(MessageConstant.INVOICE_LIST_FETCHED).build();
    }

    private InvoiceResponseDTO mapEntityToResponseDTO(InvoiceEntity invoiceEntity) {
        return InvoiceResponseDTO.builder().invoiceId(invoiceEntity.getId()).amount(invoiceEntity.getAmount())
                .paidAmount(invoiceEntity.getPaidAmount()).dueDate(invoiceEntity.getDueDate()).status(invoiceEntity.getStatus().getValue()).build();
    }

    @Override
    public ResponseDTO payInvoice(Long id, InvoicePaymentRequestDTO invoicePaymentRequestDTO) {

        // Fetch the invoice by ID, throw an exception if ID not found
        InvoiceEntity invoiceEntity = invoiceDao.getInvoiceById(id)
                .orElseThrow(() -> new InvoiceNotFoundException(MessageConstant.INVALID_INVOICE));
        Double totalPaidAmount = invoiceEntity.getPaidAmount() + invoicePaymentRequestDTO.getAmount();

        invoiceEntity.setPaidAmount(totalPaidAmount);

        // TODO: - can add the logic to validate if the due date is exceeded the current date before processing Payment

        if (invoiceEntity.getAmount().equals(totalPaidAmount)) {
            invoiceEntity.setStatus(InvoiceStatusEnum.PAID);
        }
        invoiceDao.saveInvoice(invoiceEntity);
        return ResponseDTO.builder().data(mapEntityToResponseDTO(invoiceEntity)).message(MessageConstant.PAYMENT_SUCCESS).build();
    }

    @Override
    public ResponseDTO processOverduePayment(InvoiceOverduePaymentRequestDTO invoiceOverduePaymentRequestDTO) {
        List<InvoiceEntity> overdueInvoices = invoiceDao.getAllOverdueInvoices(LocalDate.now(),
                InvoiceStatusEnum.PENDING);
        processOverDueInvoiceList(overdueInvoices, invoiceOverduePaymentRequestDTO);
        return ResponseDTO.builder().message(MessageConstant.OVERDUE_INVOICE_UPDATED).build();

    }

    private void processOverDueInvoiceList(List<InvoiceEntity> overdueInvoices, InvoiceOverduePaymentRequestDTO invoiceOverduePaymentRequestDTO) {
        List<InvoiceEntity> newInvoiceEntity = new ArrayList<>();
        List<InvoiceEntity> updateInvoiceEntity = new ArrayList<>();
        LocalDate overdueDate = LocalDate.now().plusDays(invoiceOverduePaymentRequestDTO.getOverdueDays());
        overdueInvoices.forEach(invoice ->
        {

            InvoiceStatusEnum status = invoice.getPaidAmount() > 0
                    ? InvoiceStatusEnum.PAID : InvoiceStatusEnum.VOID;

            double amountToSave = invoice.getPaidAmount() > 0
                    ? (invoice.getAmount() - invoice.getPaidAmount()) + invoiceOverduePaymentRequestDTO.getLateFee()
                    : invoice.getAmount() + invoiceOverduePaymentRequestDTO.getLateFee();

            invoice.setStatus(status);
            newInvoiceEntity.add(saveInvoiceDetails(overdueDate, amountToSave));
            updateInvoiceEntity.add(invoice);
        });
        invoiceDao.processOverDueInvoice(newInvoiceEntity, updateInvoiceEntity);
    }

    private InvoiceEntity saveInvoiceDetails(LocalDate dueDate, Double amount) {
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setDueDate(dueDate);
        invoiceEntity.setAmount(amount);
        invoiceEntity.setStatus(InvoiceStatusEnum.PENDING);
        return invoiceEntity;
    }


}