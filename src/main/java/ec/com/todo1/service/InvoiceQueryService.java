package ec.com.todo1.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import ec.com.todo1.domain.Invoice;
import ec.com.todo1.domain.*; // for static metamodels
import ec.com.todo1.repository.InvoiceRepository;
import ec.com.todo1.service.dto.InvoiceCriteria;

/**
 * Service for executing complex queries for {@link Invoice} entities in the database.
 * The main input is a {@link InvoiceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Invoice} or a {@link Page} of {@link Invoice} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvoiceQueryService extends QueryService<Invoice> {

    private final Logger log = LoggerFactory.getLogger(InvoiceQueryService.class);

    private final InvoiceRepository invoiceRepository;

    public InvoiceQueryService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * Return a {@link List} of {@link Invoice} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Invoice> findByCriteria(InvoiceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Invoice> specification = createSpecification(criteria);
        return invoiceRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Invoice} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Invoice> findByCriteria(InvoiceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Invoice> specification = createSpecification(criteria);
        return invoiceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InvoiceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Invoice> specification = createSpecification(criteria);
        return invoiceRepository.count(specification);
    }

    /**
     * Function to convert {@link InvoiceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Invoice> createSpecification(InvoiceCriteria criteria) {
        Specification<Invoice> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Invoice_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Invoice_.date));
            }
            if (criteria.getPaymentId() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentId(),
                    root -> root.join(Invoice_.payment, JoinType.LEFT).get(Payment_.id)));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Invoice_.user, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
