package com.invoicetracking.invoicemanagement.repository;

import com.invoicetracking.invoicemanagement.Enums.InvoiceStatusEnum;
import com.invoicetracking.invoicemanagement.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {

    List<InvoiceEntity> findAllByIsActive(boolean activeFlag);

    Optional<InvoiceEntity> findByIdAndIsActive(Long id, boolean activeFlag);

    List<InvoiceEntity> findByDueDateBeforeAndStatusAndIsActive(LocalDate currentDate, InvoiceStatusEnum status, boolean activeFlag);
}
