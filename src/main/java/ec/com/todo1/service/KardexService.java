package ec.com.todo1.service;

import ec.com.todo1.domain.Kardex;
import ec.com.todo1.repository.KardexRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Kardex}.
 */
@Service
@Transactional
public class KardexService {

    private final Logger log = LoggerFactory.getLogger(KardexService.class);

    private final KardexRepository kardexRepository;

    public KardexService(KardexRepository kardexRepository) {
        this.kardexRepository = kardexRepository;
    }

    /**
     * Save a kardex.
     *
     * @param kardex the entity to save.
     * @return the persisted entity.
     */
    public Kardex save(Kardex kardex) {
        log.debug("Request to save Kardex : {}", kardex);
        return kardexRepository.save(kardex);
    }

    /**
     * Get all the kardexes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Kardex> findAll(Pageable pageable) {
        log.debug("Request to get all Kardexes");
        return kardexRepository.findAll(pageable);
    }


    /**
     * Get one kardex by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Kardex> findOne(Long id) {
        log.debug("Request to get Kardex : {}", id);
        return kardexRepository.findById(id);
    }

    /**
     * Delete the kardex by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Kardex : {}", id);
        kardexRepository.deleteById(id);
    }
}
