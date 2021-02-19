package ec.com.todo1.web.rest;

import ec.com.todo1.domain.Kardex;
import ec.com.todo1.service.KardexService;
import ec.com.todo1.web.rest.errors.BadRequestAlertException;
import ec.com.todo1.service.dto.KardexCriteria;
import ec.com.todo1.service.KardexQueryService;

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
 * REST controller for managing {@link ec.com.todo1.domain.Kardex}.
 */
@RestController
@RequestMapping("/api")
public class KardexResource {

    private final Logger log = LoggerFactory.getLogger(KardexResource.class);

    private static final String ENTITY_NAME = "kardex";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KardexService kardexService;

    private final KardexQueryService kardexQueryService;

    public KardexResource(KardexService kardexService, KardexQueryService kardexQueryService) {
        this.kardexService = kardexService;
        this.kardexQueryService = kardexQueryService;
    }

    /**
     * {@code POST  /kardexes} : Create a new kardex.
     *
     * @param kardex the kardex to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new kardex, or with status {@code 400 (Bad Request)} if the kardex has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/kardexes")
    public ResponseEntity<Kardex> createKardex(@Valid @RequestBody Kardex kardex) throws URISyntaxException {
        log.debug("REST request to save Kardex : {}", kardex);
        if (kardex.getId() != null) {
            throw new BadRequestAlertException("A new kardex cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Kardex result = kardexService.save(kardex);
        return ResponseEntity.created(new URI("/api/kardexes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /kardexes} : Updates an existing kardex.
     *
     * @param kardex the kardex to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kardex,
     * or with status {@code 400 (Bad Request)} if the kardex is not valid,
     * or with status {@code 500 (Internal Server Error)} if the kardex couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/kardexes")
    public ResponseEntity<Kardex> updateKardex(@Valid @RequestBody Kardex kardex) throws URISyntaxException {
        log.debug("REST request to update Kardex : {}", kardex);
        if (kardex.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Kardex result = kardexService.save(kardex);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kardex.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /kardexes} : get all the kardexes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of kardexes in body.
     */
    @GetMapping("/kardexes")
    public ResponseEntity<List<Kardex>> getAllKardexes(KardexCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Kardexes by criteria: {}", criteria);
        Page<Kardex> page = kardexQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /kardexes/count} : count all the kardexes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/kardexes/count")
    public ResponseEntity<Long> countKardexes(KardexCriteria criteria) {
        log.debug("REST request to count Kardexes by criteria: {}", criteria);
        return ResponseEntity.ok().body(kardexQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /kardexes/:id} : get the "id" kardex.
     *
     * @param id the id of the kardex to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the kardex, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/kardexes/{id}")
    public ResponseEntity<Kardex> getKardex(@PathVariable Long id) {
        log.debug("REST request to get Kardex : {}", id);
        Optional<Kardex> kardex = kardexService.findOne(id);
        return ResponseUtil.wrapOrNotFound(kardex);
    }

    /**
     * {@code DELETE  /kardexes/:id} : delete the "id" kardex.
     *
     * @param id the id of the kardex to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/kardexes/{id}")
    public ResponseEntity<Void> deleteKardex(@PathVariable Long id) {
        log.debug("REST request to delete Kardex : {}", id);
        kardexService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
