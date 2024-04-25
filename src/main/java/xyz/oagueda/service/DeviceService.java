package xyz.oagueda.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.oagueda.domain.Device;
import xyz.oagueda.domain.Repair;
import xyz.oagueda.domain.enumeration.Status;
import xyz.oagueda.repository.DeviceRepository;
import xyz.oagueda.service.dto.DeviceDTO;
import xyz.oagueda.service.mapper.DeviceMapper;
import xyz.oagueda.web.rest.errors.DeleteDeviceWithRepairsException;

/**
 * Service Implementation for managing {@link xyz.oagueda.domain.Device}.
 */
@Service
@Transactional
public class DeviceService {

    private final Logger log = LoggerFactory.getLogger(DeviceService.class);

    private final DeviceRepository deviceRepository;

    private final DeviceMapper deviceMapper;

    private final RepairService repairService;

    public DeviceService(DeviceRepository deviceRepository, DeviceMapper deviceMapper, RepairService repairService) {
        this.deviceRepository = deviceRepository;
        this.deviceMapper = deviceMapper;
        this.repairService = repairService;
    }

    /**
     * Save a device.
     *
     * @param deviceDTO the entity to save.
     * @return the persisted entity.
     */
    public DeviceDTO save(DeviceDTO deviceDTO) {
        log.debug("Request to save Device : {}", deviceDTO);
        Device device = deviceMapper.toEntity(deviceDTO);
        device = deviceRepository.save(device);
        return deviceMapper.toDto(device);
    }

    /**
     * Update a device.
     *
     * @param deviceDTO the entity to save.
     * @return the persisted entity.
     */
    public DeviceDTO update(DeviceDTO deviceDTO) {
        log.debug("Request to update Device : {}", deviceDTO);
        Device device = deviceMapper.toEntity(deviceDTO);
        device = deviceRepository.save(device);
        return deviceMapper.toDto(device);
    }

    /**
     * Partially update a device.
     *
     * @param deviceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DeviceDTO> partialUpdate(DeviceDTO deviceDTO) {
        log.debug("Request to partially update Device : {}", deviceDTO);

        return deviceRepository
            .findById(deviceDTO.getId())
            .map(existingDevice -> {
                deviceMapper.partialUpdate(existingDevice, deviceDTO);

                return existingDevice;
            })
            .map(deviceRepository::save)
            .map(deviceMapper::toDto);
    }

    /**
     * Get one device by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DeviceDTO> findOne(Long id) {
        log.debug("Request to get Device : {}", id);
        return deviceRepository.findById(id).map(deviceMapper::toDto);
    }

    /**
     * Delete the device by id.
     *
     * @param id the id of the entity.
     * @param force if the deletion should be forced.
     */
    public void delete(Long id, boolean force) {
        log.debug("Request to delete Device : {}, force: {}", id, force);
        Device device = deviceRepository.findById(id).orElseThrow();
        if (force) {
            for (Repair repair : device.getRepairs()) {
                repairService.delete(repair.getId());
            }
        }
        for (Repair repair : device.getRepairs()) {
            if (!repair.getStatus().equals(Status.DELETED)) {
                throw new DeleteDeviceWithRepairsException();
            }
        }
        device.setDeleted(true);
    }
}
