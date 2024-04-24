package xyz.oagueda.service.mapper;

import org.mapstruct.*;
import xyz.oagueda.domain.Device;
import xyz.oagueda.domain.Repair;
import xyz.oagueda.service.dto.DeviceDTO;
import xyz.oagueda.service.dto.RepairDTO;

/**
 * Mapper for the entity {@link Repair} and its DTO {@link RepairDTO}.
 */
@Mapper(componentModel = "spring")
public interface RepairMapper extends EntityMapper<RepairDTO, Repair> {
    @Mapping(target = "device", source = "device", qualifiedByName = "deviceId")
    RepairDTO toDto(Repair s);

    @Named("deviceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DeviceDTO toDtoDeviceId(Device device);
}
