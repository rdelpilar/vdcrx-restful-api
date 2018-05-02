package com.vdcrx.rest.api.v1.mapper.entities;

import com.vdcrx.rest.api.v1.model.dto.FacilityDto;
import com.vdcrx.rest.domain.entities.Facility;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

/**
 * Facility to DTO mapper
 *
 * @author Ranel del Pilar
 */

@Mapper(componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class FacilityMapper {

    public abstract FacilityDto mapToFacilityDto(final Facility facility);

    public Facility mapToFacility(final FacilityDto dto) {
        return new Facility(dto.getFacilityType(), dto.getName());
    }

    public abstract Set<FacilityDto> mapToFacilityDtoSet(final Set<Facility> facilities);
}
