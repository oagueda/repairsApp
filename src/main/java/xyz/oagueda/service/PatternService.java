package xyz.oagueda.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.oagueda.domain.Pattern;
import xyz.oagueda.repository.PatternRepository;
import xyz.oagueda.service.dto.PatternDTO;
import xyz.oagueda.service.mapper.PatternMapper;

/**
 * Service Implementation for managing {@link xyz.oagueda.domain.Pattern}.
 */
@Service
@Transactional
public class PatternService {

    private final Logger log = LoggerFactory.getLogger(PatternService.class);

    private final PatternRepository patternRepository;

    private final PatternMapper patternMapper;

    public PatternService(PatternRepository patternRepository, PatternMapper patternMapper) {
        this.patternRepository = patternRepository;
        this.patternMapper = patternMapper;
    }

    /**
     * Save a pattern.
     *
     * @param patternDTO the entity to save.
     * @return the persisted entity.
     */
    public PatternDTO save(PatternDTO patternDTO) {
        log.debug("Request to save Pattern : {}", patternDTO);
        Pattern pattern = patternMapper.toEntity(patternDTO);
        pattern = patternRepository.save(pattern);
        return patternMapper.toDto(pattern);
    }

    /**
     * Update a pattern.
     *
     * @param patternDTO the entity to save.
     * @return the persisted entity.
     */
    public PatternDTO update(PatternDTO patternDTO) {
        log.debug("Request to update Pattern : {}", patternDTO);
        Pattern pattern = patternMapper.toEntity(patternDTO);
        pattern = patternRepository.save(pattern);
        return patternMapper.toDto(pattern);
    }

    /**
     * Partially update a pattern.
     *
     * @param patternDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PatternDTO> partialUpdate(PatternDTO patternDTO) {
        log.debug("Request to partially update Pattern : {}", patternDTO);

        return patternRepository
            .findById(patternDTO.getId())
            .map(existingPattern -> {
                patternMapper.partialUpdate(existingPattern, patternDTO);

                return existingPattern;
            })
            .map(patternRepository::save)
            .map(patternMapper::toDto);
    }

    /**
     *  Get all the patterns where Device is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PatternDTO> findAllWhereDeviceIsNull() {
        log.debug("Request to get all patterns where Device is null");
        return StreamSupport.stream(patternRepository.findAll().spliterator(), false)
            .filter(pattern -> pattern.getDevice() == null)
            .map(patternMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one pattern by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PatternDTO> findOne(Long id) {
        log.debug("Request to get Pattern : {}", id);
        return patternRepository.findById(id).map(patternMapper::toDto);
    }

    /**
     * Delete the pattern by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Pattern : {}", id);
        patternRepository.deleteById(id);
    }
}
