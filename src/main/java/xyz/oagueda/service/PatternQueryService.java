package xyz.oagueda.service;

import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import xyz.oagueda.domain.*; // for static metamodels
import xyz.oagueda.domain.Pattern;
import xyz.oagueda.repository.PatternRepository;
import xyz.oagueda.service.criteria.PatternCriteria;
import xyz.oagueda.service.dto.PatternDTO;
import xyz.oagueda.service.mapper.PatternMapper;

/**
 * Service for executing complex queries for {@link Pattern} entities in the database.
 * The main input is a {@link PatternCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PatternDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PatternQueryService extends QueryService<Pattern> {

    private final Logger log = LoggerFactory.getLogger(PatternQueryService.class);

    private final PatternRepository patternRepository;

    private final PatternMapper patternMapper;

    public PatternQueryService(PatternRepository patternRepository, PatternMapper patternMapper) {
        this.patternRepository = patternRepository;
        this.patternMapper = patternMapper;
    }

    /**
     * Return a {@link List} of {@link PatternDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PatternDTO> findByCriteria(PatternCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Pattern> specification = createSpecification(criteria);
        return patternMapper.toDto(patternRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PatternCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Pattern> specification = createSpecification(criteria);
        return patternRepository.count(specification);
    }

    /**
     * Function to convert {@link PatternCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Pattern> createSpecification(PatternCriteria criteria) {
        Specification<Pattern> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Pattern_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Pattern_.code));
            }
            if (criteria.getDeviceId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getDeviceId(), root -> root.join(Pattern_.device, JoinType.LEFT).get(Device_.id))
                );
            }
        }
        return specification;
    }
}
