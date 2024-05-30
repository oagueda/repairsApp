package xyz.oagueda.service.mapper;

import org.mapstruct.*;
import xyz.oagueda.domain.Device;
import xyz.oagueda.service.dto.DeviceDTO;

/**
 * Mapper for the entity {@link Device} and its DTO {@link DeviceDTO}.
 */
@Mapper(componentModel = "spring")
public interface DeviceMapper extends EntityMapper<DeviceDTO, Device> {}
