package xyz.oagueda.service;

import java.io.ByteArrayOutputStream;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.oagueda.domain.Repair;
import xyz.oagueda.domain.enumeration.Status;
import xyz.oagueda.repository.RepairRepository;
import xyz.oagueda.service.dto.RepairDTO;
import xyz.oagueda.service.mapper.RepairMapper;

/**
 * Service Implementation for managing {@link xyz.oagueda.domain.Repair}.
 */
@Service
@Transactional
public class RepairService {

    private final Logger log = LoggerFactory.getLogger(RepairService.class);

    private final RepairRepository repairRepository;

    private final RepairMapper repairMapper;

    private final RepairPrintService repairPrintService;

    public RepairService(RepairRepository repairRepository, RepairMapper repairMapper, RepairPrintService repairPrintService) {
        this.repairRepository = repairRepository;
        this.repairMapper = repairMapper;
        this.repairPrintService = repairPrintService;
    }

    /**
     * Save a repair.
     *
     * @param repairDTO the entity to save.
     * @return the persisted entity.
     */
    public RepairDTO save(RepairDTO repairDTO) {
        log.debug("Request to save Repair : {}", repairDTO);
        Repair repair = repairMapper.toEntity(repairDTO);
        repair = repairRepository.save(repair);
        return repairMapper.toDto(repair);
    }

    /**
     * Update a repair.
     *
     * @param repairDTO the entity to save.
     * @return the persisted entity.
     */
    public RepairDTO update(RepairDTO repairDTO) {
        log.debug("Request to update Repair : {}", repairDTO);
        Repair repair = repairMapper.toEntity(repairDTO);
        repair = repairRepository.save(repair);
        return repairMapper.toDto(repair);
    }

    /**
     * Partially update a repair.
     *
     * @param repairDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RepairDTO> partialUpdate(RepairDTO repairDTO) {
        log.debug("Request to partially update Repair : {}", repairDTO);

        return repairRepository
            .findById(repairDTO.getId())
            .map(existingRepair -> {
                repairMapper.partialUpdate(existingRepair, repairDTO);

                return existingRepair;
            })
            .map(repairRepository::save)
            .map(repairMapper::toDto);
    }

    /**
     * Get one repair by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RepairDTO> findOne(Long id) {
        log.debug("Request to get Repair : {}", id);
        return repairRepository.findById(id).map(repairMapper::toDto);
    }

    /**
     * Delete the repair by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Repair : {}", id);
        repairRepository.findById(id).orElseThrow().setStatus(Status.DELETED);
    }

    /**
     * Print one repair by id.
     *
     * @param id the id of the entity.
     * @return the pdf file.
     */
    @Transactional(readOnly = true)
    public ByteArrayOutputStream print(RepairDTO repairDTO) {
        log.debug("Request to print Repair : {}", repairDTO);
        return repairPrintService.printRepair(repairDTO);
    }
}
