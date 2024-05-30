package xyz.oagueda.service;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.metamodel.SingularAttribute;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.oagueda.domain.*; // for static metamodels
import xyz.oagueda.repository.RepairRepository;
import xyz.oagueda.service.criteria.RepairCriteria;
import xyz.oagueda.service.dto.RepairDTO;
import xyz.oagueda.service.mapper.RepairMapper;

/**
 * Service for executing complex queries for {@link Repair} entities in the database.
 * The main input is a {@link RepairCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link RepairDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RepairQueryService extends SpecQueryService<Repair> {

    private final Logger log = LoggerFactory.getLogger(RepairQueryService.class);

    private final RepairRepository repairRepository;

    private final RepairMapper repairMapper;

    public RepairQueryService(RepairRepository repairRepository, RepairMapper repairMapper) {
        this.repairRepository = repairRepository;
        this.repairMapper = repairMapper;
    }

    /**
     * Return a {@link Page} of {@link RepairDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RepairDTO> findByCriteria(RepairCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Repair> specification = createSpecification(criteria);
        return repairRepository.findAll(specification, page).map(repairMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RepairCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Repair> specification = createSpecification(criteria);
        return repairRepository.count(specification);
    }

    /**
     * Function to convert {@link RepairCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Repair> createSpecification(RepairCriteria criteria) {
        Specification<Repair> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Repair_.id));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Repair_.status));
            }
            if (criteria.getClosedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getClosedDate(), Repair_.closedDate));
            }
            if (criteria.getBudget() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBudget(), Repair_.budget));
            }
            if (criteria.getImportantData() != null) {
                specification = specification.and(buildSpecification(criteria.getImportantData(), Repair_.importantData));
            }
            if (criteria.getTotal() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTotal(), Repair_.total));
            }
            if (criteria.getDeviceId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getDeviceId(), root -> root.join(Repair_.device, JoinType.LEFT).get(Device_.id))
                );
            }

            if (criteria.getFilter() != null) {
                List<SingularAttribute<Repair, ?>> defaultColumns = Arrays.asList(
                    Repair_.id,
                    Repair_.description,
                    Repair_.status,
                    Repair_.closedDate
                );
                Map<String, String> referencedColumns = Map.of("device", "type");

                specification = specification.and(createLikeFilter(criteria.getFilter(), defaultColumns, referencedColumns));
            }
        }
        return specification;
    }
}
