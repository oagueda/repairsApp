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
import xyz.oagueda.repository.DeviceRepository;
import xyz.oagueda.service.criteria.DeviceCriteria;
import xyz.oagueda.service.dto.DeviceDTO;
import xyz.oagueda.service.mapper.DeviceMapper;

/**
 * Service for executing complex queries for {@link Device} entities in the database.
 * The main input is a {@link DeviceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DeviceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DeviceQueryService extends SpecQueryService<Device> {

    private final Logger log = LoggerFactory.getLogger(DeviceQueryService.class);

    private final DeviceRepository deviceRepository;

    private final DeviceMapper deviceMapper;

    public DeviceQueryService(DeviceRepository deviceRepository, DeviceMapper deviceMapper) {
        this.deviceRepository = deviceRepository;
        this.deviceMapper = deviceMapper;
    }

    /**
     * Return a {@link Page} of {@link DeviceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DeviceDTO> findByCriteria(DeviceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Device> specification = createSpecification(criteria);
        return deviceRepository.findAll(specification, page).map(deviceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DeviceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Device> specification = createSpecification(criteria);
        return deviceRepository.count(specification);
    }

    /**
     * Function to convert {@link DeviceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Device> createSpecification(DeviceCriteria criteria) {
        Specification<Device> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Device_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Device_.type));
            }
            if (criteria.getBrand() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBrand(), Device_.brand));
            }
            if (criteria.getModel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModel(), Device_.model));
            }
            if (criteria.getSerialNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSerialNumber(), Device_.serialNumber));
            }
            if (criteria.getWarranty() != null) {
                specification = specification.and(buildSpecification(criteria.getWarranty(), Device_.warranty));
            }
            if (criteria.getPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPassword(), Device_.password));
            }
            if (criteria.getHasPattern() != null) {
                specification = specification.and(buildSpecification(criteria.getHasPattern(), Device_.hasPattern));
            }
            if (criteria.getSimPinCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSimPinCode(), Device_.simPinCode));
            }
            if (criteria.getDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getDeleted(), Device_.deleted));
            }
            if (criteria.getPatternId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPatternId(), root -> root.join(Device_.pattern, JoinType.LEFT).get(Pattern_.id))
                );
            }
            if (criteria.getRepairId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getRepairId(), root -> root.join(Device_.repairs, JoinType.LEFT).get(Repair_.id))
                );
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root -> root.join(Device_.customer, JoinType.LEFT).get(Customer_.id))
                );
            }

            if (criteria.getFilter() != null) {
                List<SingularAttribute<Device, ?>> defaultColumns = Arrays.asList(
                    Device_.id,
                    Device_.type,
                    Device_.brand,
                    Device_.model,
                    Device_.notes
                );
                Map<String, String> referencedColumns = Map.of("customer", "name,nif");

                specification = specification.and(createLikeFilter(criteria.getFilter(), defaultColumns, referencedColumns));
            }
        }
        return specification;
    }
}
