package xyz.oagueda.service.mapper;

import org.mapstruct.*;
import xyz.oagueda.domain.Customer;
import xyz.oagueda.service.dto.CustomerDTO;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {}
