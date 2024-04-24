package xyz.oagueda.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import xyz.oagueda.repository.PatternRepository;
import xyz.oagueda.service.PatternQueryService;
import xyz.oagueda.service.PatternService;
import xyz.oagueda.service.criteria.PatternCriteria;
import xyz.oagueda.service.dto.PatternDTO;
import xyz.oagueda.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.oagueda.domain.Pattern}.
 */
@RestController
@RequestMapping("/api/patterns")
public class PatternResource {

    private final Logger log = LoggerFactory.getLogger(PatternResource.class);

    private static final String ENTITY_NAME = "pattern";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PatternService patternService;

    private final PatternRepository patternRepository;

    private final PatternQueryService patternQueryService;

    public PatternResource(PatternService patternService, PatternRepository patternRepository, PatternQueryService patternQueryService) {
        this.patternService = patternService;
        this.patternRepository = patternRepository;
        this.patternQueryService = patternQueryService;
    }

    /**
     * {@code POST  /patterns} : Create a new pattern.
     *
     * @param patternDTO the patternDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new patternDTO, or with status {@code 400 (Bad Request)} if the pattern has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PatternDTO> createPattern(@RequestBody PatternDTO patternDTO) throws URISyntaxException {
        log.debug("REST request to save Pattern : {}", patternDTO);
        if (patternDTO.getId() != null) {
            throw new BadRequestAlertException("A new pattern cannot already have an ID", ENTITY_NAME, "idexists");
        }
        patternDTO = patternService.save(patternDTO);
        return ResponseEntity.created(new URI("/api/patterns/" + patternDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, patternDTO.getId().toString()))
            .body(patternDTO);
    }

    /**
     * {@code PUT  /patterns/:id} : Updates an existing pattern.
     *
     * @param id the id of the patternDTO to save.
     * @param patternDTO the patternDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patternDTO,
     * or with status {@code 400 (Bad Request)} if the patternDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the patternDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PatternDTO> updatePattern(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PatternDTO patternDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Pattern : {}, {}", id, patternDTO);
        if (patternDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patternDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patternRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        patternDTO = patternService.update(patternDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patternDTO.getId().toString()))
            .body(patternDTO);
    }

    /**
     * {@code PATCH  /patterns/:id} : Partial updates given fields of an existing pattern, field will ignore if it is null
     *
     * @param id the id of the patternDTO to save.
     * @param patternDTO the patternDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patternDTO,
     * or with status {@code 400 (Bad Request)} if the patternDTO is not valid,
     * or with status {@code 404 (Not Found)} if the patternDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the patternDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PatternDTO> partialUpdatePattern(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PatternDTO patternDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pattern partially : {}, {}", id, patternDTO);
        if (patternDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patternDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patternRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PatternDTO> result = patternService.partialUpdate(patternDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patternDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /patterns} : get all the patterns.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of patterns in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PatternDTO>> getAllPatterns(PatternCriteria criteria) {
        log.debug("REST request to get Patterns by criteria: {}", criteria);

        List<PatternDTO> entityList = patternQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /patterns/count} : count all the patterns.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPatterns(PatternCriteria criteria) {
        log.debug("REST request to count Patterns by criteria: {}", criteria);
        return ResponseEntity.ok().body(patternQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /patterns/:id} : get the "id" pattern.
     *
     * @param id the id of the patternDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the patternDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PatternDTO> getPattern(@PathVariable("id") Long id) {
        log.debug("REST request to get Pattern : {}", id);
        Optional<PatternDTO> patternDTO = patternService.findOne(id);
        return ResponseUtil.wrapOrNotFound(patternDTO);
    }

    /**
     * {@code DELETE  /patterns/:id} : delete the "id" pattern.
     *
     * @param id the id of the patternDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePattern(@PathVariable("id") Long id) {
        log.debug("REST request to delete Pattern : {}", id);
        patternService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
