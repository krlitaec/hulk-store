package ec.com.todo1.repository;

import ec.com.todo1.domain.DetailInvoice;

import ec.com.todo1.domain.Invoice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Spring Data  repository for the DetailInvoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetailInvoiceRepository extends JpaRepository<DetailInvoice, Long>, JpaSpecificationExecutor<DetailInvoice> {

    Set<DetailInvoice> findByInvoice(Invoice invoice);
}
