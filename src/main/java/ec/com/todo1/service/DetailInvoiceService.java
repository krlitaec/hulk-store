package ec.com.todo1.service;

import ec.com.todo1.domain.DetailInvoice;
import ec.com.todo1.domain.Invoice;
import ec.com.todo1.domain.Kardex;
import ec.com.todo1.domain.Product;
import ec.com.todo1.repository.DetailInvoiceRepository;
import ec.com.todo1.repository.KardexRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing {@link DetailInvoice}.
 */
@Service
@Transactional
public class DetailInvoiceService {

    private final Logger log = LoggerFactory.getLogger(DetailInvoiceService.class);

    private final DetailInvoiceRepository detailInvoiceRepository;

    private final KardexRepository kardexRepository;

    public DetailInvoiceService(DetailInvoiceRepository detailInvoiceRepository, KardexRepository kardexRepository) {
        this.detailInvoiceRepository = detailInvoiceRepository;
        this.kardexRepository = kardexRepository;
    }

    /**
     * Save a detailInvoice.
     *
     * @param detailInvoice the entity to save.
     * @return the persisted entity.
     */
    public DetailInvoice save(DetailInvoice detailInvoice) {
        log.debug("Request to save DetailInvoice : {}", detailInvoice);
        Product product = detailInvoice.getProduct();
        Kardex kardex = kardexRepository.findLastByProduct(product.getId());
        if (kardex != null) {
            Integer currentStock = kardex.getCurrentStock();
            Integer quantityToKardex = currentStock - detailInvoice.getQuantity();
            Kardex nuevoKardex = new Kardex();
            nuevoKardex.setProduct(product);
            nuevoKardex.setComments("Add Invoice Detail, Invoice: " + detailInvoice.getInvoice().getId());
            nuevoKardex.setType("O");
            nuevoKardex.setQuantity(detailInvoice.getQuantity());
            nuevoKardex.setRegularPrice(product.getRegularPrice());
            nuevoKardex.setSalePrice(product.getSalePrice());
            nuevoKardex.setCurrentStock(quantityToKardex);
            kardexRepository.save(nuevoKardex);
        }
        return detailInvoiceRepository.save(detailInvoice);
    }

    /**
     * Get all the detailInvoices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DetailInvoice> findAll(Pageable pageable) {
        log.debug("Request to get all DetailInvoices");
        return detailInvoiceRepository.findAll(pageable);
    }


    /**
     * Get one detailInvoice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DetailInvoice> findOne(Long id) {
        log.debug("Request to get DetailInvoice : {}", id);
        return detailInvoiceRepository.findById(id);
    }

    /**
     * Delete the detailInvoice by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DetailInvoice : {}", id);
        detailInvoiceRepository.deleteById(id);
    }

    /**
     * Delete All
     *
     * @param invoice the id of the entity.
     */
    public void deleteAllInvoice(Invoice invoice) {
        Set<DetailInvoice> details = findByInvoice(invoice);
        for (DetailInvoice det : details) {
            Product product = det.getProduct();
            Kardex kardex = kardexRepository.findLastByProduct(product.getId());
            if (kardex != null) {
                Integer currentStock = kardex.getCurrentStock();
                Integer quantityToKardex = currentStock + det.getQuantity();
                Kardex nuevoKardex = new Kardex();
                nuevoKardex.setProduct(product);
                nuevoKardex.setComments("Delete Invoice Detail, Invoice: " + det.getInvoice().getId());
                nuevoKardex.setType("I");
                nuevoKardex.setQuantity(det.getQuantity());
                nuevoKardex.setRegularPrice(product.getRegularPrice());
                nuevoKardex.setSalePrice(product.getSalePrice());
                nuevoKardex.setCurrentStock(quantityToKardex);
                kardexRepository.save(nuevoKardex);
            }
        }
        detailInvoiceRepository.deleteAll(details);
    }

    /**
     * findByInvoice.
     *
     * @param invoice the entity.
     * @return the persisted entity.
     */
    public Set<DetailInvoice> findByInvoice(Invoice invoice) {
        return detailInvoiceRepository.findByInvoice(invoice);
    }
}
