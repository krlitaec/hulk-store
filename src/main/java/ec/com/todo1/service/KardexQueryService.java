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

import ec.com.todo1.domain.Kardex;
import ec.com.todo1.domain.*; // for static metamodels
import ec.com.todo1.repository.KardexRepository;
import ec.com.todo1.service.dto.KardexCriteria;

/**
 * Service for executing complex queries for {@link Kardex} entities in the database.
 * The main input is a {@link KardexCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Kardex} or a {@link Page} of {@link Kardex} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class KardexQueryService extends QueryService<Kardex> {

    private final Logger log = LoggerFactory.getLogger(KardexQueryService.class);

    private final KardexRepository kardexRepository;

    public KardexQueryService(KardexRepository kardexRepository) {
        this.kardexRepository = kardexRepository;
    }

    /**
     * Return a {@link List} of {@link Kardex} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Kardex> findByCriteria(KardexCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Kardex> specification = createSpecification(criteria);
        return kardexRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Kardex} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Kardex> findByCriteria(KardexCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Kardex> specification = createSpecification(criteria);
        return kardexRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(KardexCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Kardex> specification = createSpecification(criteria);
        return kardexRepository.count(specification);
    }

    /**
     * Function to convert {@link KardexCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Kardex> createSpecification(KardexCriteria criteria) {
        Specification<Kardex> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Kardex_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Kardex_.type));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), Kardex_.quantity));
            }
            if (criteria.getComments() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComments(), Kardex_.comments));
            }
            if (criteria.getRegularPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRegularPrice(), Kardex_.regularPrice));
            }
            if (criteria.getSalePrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSalePrice(), Kardex_.salePrice));
            }
            if (criteria.getCurrentStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCurrentStock(), Kardex_.currentStock));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(buildSpecification(criteria.getProductId(),
                    root -> root.join(Kardex_.product, JoinType.LEFT).get(Product_.id)));
            }
        }
        return specification;
    }
}
