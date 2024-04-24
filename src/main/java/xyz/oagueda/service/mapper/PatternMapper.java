package xyz.oagueda.service.mapper;

import org.mapstruct.*;
import xyz.oagueda.domain.Pattern;
import xyz.oagueda.service.dto.PatternDTO;

/**
 * Mapper for the entity {@link Pattern} and its DTO {@link PatternDTO}.
 */
@Mapper(componentModel = "spring")
public interface PatternMapper extends EntityMapper<PatternDTO, Pattern> {}
