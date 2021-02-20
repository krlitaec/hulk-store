package ec.com.todo1.web.rest;

import ec.com.todo1.domain.DetailInvoice;
import ec.com.todo1.service.DetailInvoiceService;
import ec.com.todo1.web.rest.errors.BadRequestAlertException;
import ec.com.todo1.service.dto.DetailInvoiceCriteria;
import ec.com.todo1.service.DetailInvoiceQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link ec.com.todo1.domain.DetailInvoice}.
 */
@RestController
@RequestMapping("/api")
public class DetailInvoiceResource {

    private final Logger log = LoggerFactory.getLogger(DetailInvoiceResource.class);

    private static final String ENTITY_NAME = "detailInvoice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DetailInvoiceService detailInvoiceService;

    private final DetailInvoiceQueryService detailInvoiceQueryService;

    public DetailInvoiceResource(DetailInvoiceService detailInvoiceService, DetailInvoiceQueryService detailInvoiceQueryService) {
        this.detailInvoiceService = detailInvoiceService;
        this.detailInvoiceQueryService = detailInvoiceQueryService;
    }

    /**
     * {@code POST  /detail-invoices} : Create a new detailInvoice.
     *
     * @param detailInvoice the detailInvoice to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new detailInvoice, or with status {@code 400 (Bad Request)} if the detailInvoice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/detail-invoices")
    public ResponseEntity<DetailInvoice> createDetailInvoice(@Valid @RequestBody DetailInvoice detailInvoice) throws URISyntaxException {
        log.debug("REST request to save DetailInvoice : {}", detailInvoice);
        if (detailInvoice.getId() != null) {
            throw new BadRequestAlertException("A new detailInvoice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DetailInvoice result = detailInvoiceService.save(detailInvoice);
        return ResponseEntity.created(new URI("/api/detail-invoices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /detail-invoices} : Updates an existing detailInvoice.
     *
     * @param detailInvoice the detailInvoice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detailInvoice,
     * or with status {@code 400 (Bad Request)} if the detailInvoice is not valid,
     * or with status {@code 500 (Internal Server Error)} if the detailInvoice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/detail-invoices")
    public ResponseEntity<DetailInvoice> updateDetailInvoice(@Valid @RequestBody DetailInvoice detailInvoice) throws URISyntaxException {
        log.debug("REST request to update DetailInvoice : {}", detailInvoice);
        if (detailInvoice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DetailInvoice result = detailInvoiceService.save(detailInvoice);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detailInvoice.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /detail-invoices} : get all the detailInvoices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of detailInvoices in body.
     */
    @GetMapping("/detail-invoices")
    public ResponseEntity<List<DetailInvoice>> getAllDetailInvoices(DetailInvoiceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DetailInvoices by criteria: {}", criteria);
        Page<DetailInvoice> page = detailInvoiceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /detail-invoices/count} : count all the detailInvoices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/detail-invoices/count")
    public ResponseEntity<Long> countDetailInvoices(DetailInvoiceCriteria criteria) {
        log.debug("REST request to count DetailInvoices by criteria: {}", criteria);
        return ResponseEntity.ok().body(detailInvoiceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /detail-invoices/:id} : get the "id" detailInvoice.
     *
     * @param id the id of the detailInvoice to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the detailInvoice, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/detail-invoices/{id}")
    public ResponseEntity<DetailInvoice> getDetailInvoice(@PathVariable Long id) {
        log.debug("REST request to get DetailInvoice : {}", id);
        Optional<DetailInvoice> detailInvoice = detailInvoiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(detailInvoice);
    }

    /**
     * {@code DELETE  /detail-invoices/:id} : delete the "id" detailInvoice.
     *
     * @param id the id of the detailInvoice to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/detail-invoices/{id}")
    public ResponseEntity<Void> deleteDetailInvoice(@PathVariable Long id) {
        log.debug("REST request to delete DetailInvoice : {}", id);
        detailInvoiceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
