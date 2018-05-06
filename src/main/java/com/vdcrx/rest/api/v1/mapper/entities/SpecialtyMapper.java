package com.vdcrx.rest.api.v1.mapper.entities;

import com.vdcrx.rest.api.v1.model.dto.SpecialtyDto;
import com.vdcrx.rest.domain.entities.Person;
import com.vdcrx.rest.domain.entities.Specialty;
import com.vdcrx.rest.domain.entities.Veterinarian;
import com.vdcrx.rest.domain.entities.VeterinarianSpecialty;
import com.vdcrx.rest.domain.enums.VetSpecialtyType;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * SpecialtyService to DTO mapper
 *
 * @author Ranel del Pilar
 */

@Mapper(componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class SpecialtyMapper {

    public SpecialtyDto mapToSpecialtyDto(final Specialty specialty) {

        SpecialtyDto dto = new SpecialtyDto();
        dto.setId(specialty.getId());

        if(specialty instanceof VeterinarianSpecialty)
            dto.setSpecialty(((VeterinarianSpecialty) specialty).getVetSpecialtyType().name());

        return dto;
    }

    public Specialty mapToSpecialty(final SpecialtyDto dto) {
        return new VeterinarianSpecialty(VetSpecialtyType.valueOf(dto.getSpecialty()));
    }

    public Specialty mapToSpecialty(final Person person, final SpecialtyDto dto) {

        if(person instanceof Veterinarian) {
            return new VeterinarianSpecialty(VetSpecialtyType.valueOf(dto.getSpecialty()));
        }

        return null;
    }
}
