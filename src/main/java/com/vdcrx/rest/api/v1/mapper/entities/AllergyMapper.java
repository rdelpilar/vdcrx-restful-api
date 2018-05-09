package com.vdcrx.rest.api.v1.mapper.entities;

import com.vdcrx.rest.api.v1.model.dto.PetAllergyDto;
import com.vdcrx.rest.domain.entities.Allergy;
import com.vdcrx.rest.domain.entities.PetAllergy;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Allergy mapper
 *
 * @author Ranel del Pilar
 */

@Mapper(componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class AllergyMapper {

    public PetAllergyDto mapToAllergyDto(final Allergy allergy) {

        PetAllergyDto dto = new PetAllergyDto();
        dto.setId(allergy.getId());

        if(allergy instanceof PetAllergy) {
            dto.setPetAllergyType(((PetAllergy) allergy).getPetAllergyType());
        }

        return dto;
    }

    public Allergy mapToAllergy(final PetAllergyDto dto) {
        return new PetAllergy(dto.getPetAllergyType());
    }
}
