package com.vdcrx.rest.api.v1.mapper.entities;

import com.vdcrx.rest.api.v1.model.dto.ProviderIdDto;
import com.vdcrx.rest.domain.entities.ProviderId;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

/**
 * Provider identifier to Dto mapper
 *
 * @author Ranel del Pilar
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProviderIdMapper {
    ProviderIdDto mapToProviderIdDto(ProviderId providerId);
    ProviderId mapToProviderId(ProviderIdDto dto);

    Set<ProviderIdDto> mapToProviderIdDtoSet(Set<ProviderId> providerIds);
}


