package xyz.oagueda.service.mapper;

import org.mapstruct.*;
import xyz.oagueda.domain.Customer;
import xyz.oagueda.domain.Device;
import xyz.oagueda.domain.Pattern;
import xyz.oagueda.service.dto.CustomerDTO;
import xyz.oagueda.service.dto.DeviceDTO;
import xyz.oagueda.service.dto.PatternDTO;

/**
 * Mapper for the entity {@link Device} and its DTO {@link DeviceDTO}.
 */
@Mapper(componentModel = "spring")
public interface DeviceMapper extends EntityMapper<DeviceDTO, Device> {
    @Mapping(target = "pattern", source = "pattern", qualifiedByName = "patternId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    DeviceDTO toDto(Device s);

    @Named("patternId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PatternDTO toDtoPatternId(Pattern pattern);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);
}
