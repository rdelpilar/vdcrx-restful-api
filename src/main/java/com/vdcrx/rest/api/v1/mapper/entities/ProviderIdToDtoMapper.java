package com.vdcrx.rest.api.v1.mapper.entities;

import com.vdcrx.rest.api.v1.model.dto.ProviderIdDto;
import com.vdcrx.rest.domain.entities.ProviderId;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Provider identifier to Dto mapper
 *
 * @author Ranel del Pilar
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProviderIdToDtoMapper {
    ProviderIdDto mapToProviderIdDto(ProviderId providerId);
}


