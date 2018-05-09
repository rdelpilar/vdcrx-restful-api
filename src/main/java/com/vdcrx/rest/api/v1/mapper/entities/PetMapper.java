package com.vdcrx.rest.api.v1.mapper.entities;

import com.vdcrx.rest.api.v1.model.dto.PetDto;
import com.vdcrx.rest.domain.entities.Pet;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Pet mapper
 *
 * @author Ranel del Pilar
 */

@Mapper(componentModel = "spring",
        uses = {AllergyMapper.class, MedicationMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PetMapper {
    PetDto mapToPetDto(final Pet pet);
    Pet mapToPet(final PetDto dto);
}
