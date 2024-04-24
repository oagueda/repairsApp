package xyz.oagueda.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import xyz.oagueda.repository.RepairRepository;
import xyz.oagueda.service.RepairQueryService;
import xyz.oagueda.service.RepairService;
import xyz.oagueda.service.criteria.RepairCriteria;
import xyz.oagueda.service.dto.RepairDTO;
import xyz.oagueda.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.oagueda.domain.Repair}.
 */
@RestController
@RequestMapping("/api/repairs")
public class RepairResource {

    private final Logger log = LoggerFactory.getLogger(RepairResource.class);

    private static final String ENTITY_NAME = "repair";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RepairService repairService;

    private final RepairRepository repairRepository;

    private final RepairQueryService repairQueryService;

    public RepairResource(RepairService repairService, RepairRepository repairRepository, RepairQueryService repairQueryService) {
        this.repairService = repairService;
        this.repairRepository = repairRepository;
        this.repairQueryService = repairQueryService;
    }

    /**
     * {@code POST  /repairs} : Create a new repair.
     *
     * @param repairDTO the repairDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new repairDTO, or with status {@code 400 (Bad Request)} if the repair has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RepairDTO> createRepair(@Valid @RequestBody RepairDTO repairDTO) throws URISyntaxException {
        log.debug("REST request to save Repair : {}", repairDTO);
        if (repairDTO.getId() != null) {
            throw new BadRequestAlertException("A new repair cannot already have an ID", ENTITY_NAME, "idexists");
        }
        repairDTO = repairService.save(repairDTO);
        return ResponseEntity.created(new URI("/api/repairs/" + repairDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, repairDTO.getId().toString()))
            .body(repairDTO);
    }

    /**
     * {@code PUT  /repairs/:id} : Updates an existing repair.
     *
     * @param id the id of the repairDTO to save.
     * @param repairDTO the repairDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated repairDTO,
     * or with status {@code 400 (Bad Request)} if the repairDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the repairDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RepairDTO> updateRepair(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RepairDTO repairDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Repair : {}, {}", id, repairDTO);
        if (repairDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, repairDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!repairRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        repairDTO = repairService.update(repairDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, repairDTO.getId().toString()))
            .body(repairDTO);
    }

    /**
     * {@code PATCH  /repairs/:id} : Partial updates given fields of an existing repair, field will ignore if it is null
     *
     * @param id the id of the repairDTO to save.
     * @param repairDTO the repairDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated repairDTO,
     * or with status {@code 400 (Bad Request)} if the repairDTO is not valid,
     * or with status {@code 404 (Not Found)} if the repairDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the repairDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RepairDTO> partialUpdateRepair(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RepairDTO repairDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Repair partially : {}, {}", id, repairDTO);
        if (repairDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, repairDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!repairRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RepairDTO> result = repairService.partialUpdate(repairDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, repairDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /repairs} : get all the repairs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of repairs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RepairDTO>> getAllRepairs(
        RepairCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Repairs by criteria: {}", criteria);

        Page<RepairDTO> page = repairQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /repairs/count} : count all the repairs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countRepairs(RepairCriteria criteria) {
        log.debug("REST request to count Repairs by criteria: {}", criteria);
        return ResponseEntity.ok().body(repairQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /repairs/:id} : get the "id" repair.
     *
     * @param id the id of the repairDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the repairDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RepairDTO> getRepair(@PathVariable("id") Long id) {
        log.debug("REST request to get Repair : {}", id);
        Optional<RepairDTO> repairDTO = repairService.findOne(id);
        return ResponseUtil.wrapOrNotFound(repairDTO);
    }

    /**
     * {@code DELETE  /repairs/:id} : delete the "id" repair.
     *
     * @param id the id of the repairDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRepair(@PathVariable("id") Long id) {
        log.debug("REST request to delete Repair : {}", id);
        repairService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
