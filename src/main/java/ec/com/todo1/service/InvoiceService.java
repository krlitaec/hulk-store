package ec.com.todo1.service;

import ec.com.todo1.domain.DetailInvoice;
import ec.com.todo1.domain.Invoice;
import ec.com.todo1.repository.InvoiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing {@link Invoice}.
 */
@Service
@Transactional
public class InvoiceService {

    private final Logger log = LoggerFactory.getLogger(InvoiceService.class);

    private final InvoiceRepository invoiceRepository;

    private final DetailInvoiceService detailInvoiceService;

    public InvoiceService(InvoiceRepository invoiceRepository, DetailInvoiceService detailInvoiceService) {
        this.invoiceRepository = invoiceRepository;
        this.detailInvoiceService = detailInvoiceService;
    }

    /**
     * Save a invoice.
     *
     * @param invoice the entity to save.
     * @return the persisted entity.
     */
    public Invoice save(Invoice invoice) {
        log.debug("Request to save Invoice : {}", invoice);
        Set<DetailInvoice> details = invoice.getDetailInvoices();
        invoice.setDetailInvoices(null);
        invoiceRepository.save(invoice);
        BigDecimal total = BigDecimal.ZERO;
        detailInvoiceService.deleteAllInvoice(invoice);
        for (DetailInvoice det : details) {
            DetailInvoice detailInvoice = new DetailInvoice();
            detailInvoice.setProduct(det.getProduct());
            detailInvoice.setInvoice(invoice);
            detailInvoice.setQuantity(1);
            detailInvoice.setPrice(det.getProduct().getSalePrice());
            detailInvoice.setTotal(det.getProduct().getSalePrice());
            total = total.add(detailInvoice.getTotal());
            detailInvoiceService.save(detailInvoice);
        }
        invoice.setTotal(total);
        return invoiceRepository.save(invoice);
    }

    /**
     * Get all the invoices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Invoice> findAll(Pageable pageable) {
        log.debug("Request to get all Invoices");
        return invoiceRepository.findAll(pageable);
    }


    /**
     * Get one invoice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Invoice> findOne(Long id) {
        log.debug("Request to get Invoice : {}", id);
        return invoiceRepository.findById(id);
    }

    /**
     * Delete the invoice by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Invoice : {}", id);
        invoiceRepository.deleteById(id);
    }

    /**
     * Get one invoice by id.
     *
     * @return the entity.
     */
    public Invoice findActiveByUser(Long idUser) {
        return invoiceRepository.findActiveByUser(idUser);
    }
}
