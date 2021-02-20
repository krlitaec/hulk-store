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

import ec.com.todo1.domain.DetailInvoice;
import ec.com.todo1.domain.*; // for static metamodels
import ec.com.todo1.repository.DetailInvoiceRepository;
import ec.com.todo1.service.dto.DetailInvoiceCriteria;

/**
 * Service for executing complex queries for {@link DetailInvoice} entities in the database.
 * The main input is a {@link DetailInvoiceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DetailInvoice} or a {@link Page} of {@link DetailInvoice} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DetailInvoiceQueryService extends QueryService<DetailInvoice> {

    private final Logger log = LoggerFactory.getLogger(DetailInvoiceQueryService.class);

    private final DetailInvoiceRepository detailInvoiceRepository;

    public DetailInvoiceQueryService(DetailInvoiceRepository detailInvoiceRepository) {
        this.detailInvoiceRepository = detailInvoiceRepository;
    }

    /**
     * Return a {@link List} of {@link DetailInvoice} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DetailInvoice> findByCriteria(DetailInvoiceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DetailInvoice> specification = createSpecification(criteria);
        return detailInvoiceRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DetailInvoice} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DetailInvoice> findByCriteria(DetailInvoiceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DetailInvoice> specification = createSpecification(criteria);
        return detailInvoiceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DetailInvoiceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DetailInvoice> specification = createSpecification(criteria);
        return detailInvoiceRepository.count(specification);
    }

    /**
     * Function to convert {@link DetailInvoiceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DetailInvoice> createSpecification(DetailInvoiceCriteria criteria) {
        Specification<DetailInvoice> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DetailInvoice_.id));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), DetailInvoice_.quantity));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), DetailInvoice_.price));
            }
            if (criteria.getTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotal(), DetailInvoice_.total));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(buildSpecification(criteria.getProductId(),
                    root -> root.join(DetailInvoice_.product, JoinType.LEFT).get(Product_.id)));
            }
            if (criteria.getInvoiceId() != null) {
                specification = specification.and(buildSpecification(criteria.getInvoiceId(),
                    root -> root.join(DetailInvoice_.invoice, JoinType.LEFT).get(Invoice_.id)));
            }
        }
        return specification;
    }
}
