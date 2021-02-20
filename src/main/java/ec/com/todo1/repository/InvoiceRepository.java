package ec.com.todo1.repository;

import ec.com.todo1.domain.Invoice;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Invoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long>, JpaSpecificationExecutor<Invoice> {

    @Query("select invoice from Invoice invoice where invoice.user.login = ?#{principal.username}")
    List<Invoice> findByUserIsCurrentUser();

    @Query(value="SELECT * FROM invoice WHERE payment_id is null and user_id = ?1", nativeQuery = true)
    Invoice findActiveByUser(Long idUser);
}
