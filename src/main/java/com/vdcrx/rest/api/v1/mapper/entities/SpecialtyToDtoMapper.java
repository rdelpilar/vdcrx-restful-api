package com.vdcrx.rest.api.v1.mapper.entities;

import com.vdcrx.rest.api.v1.model.dto.SpecialtyDto;
import com.vdcrx.rest.domain.entities.Specialty;
import com.vdcrx.rest.domain.entities.VeterinarianSpecialty;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Specialty to DTO mapper
 *
 * @author Ranel del Pilar
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class SpecialtyToDtoMapper {
    public SpecialtyDto mapToSpecialtyDto(Specialty specialty) {

        SpecialtyDto dto = new SpecialtyDto();
        String name;

        if(specialty instanceof VeterinarianSpecialty) {
            name = ((VeterinarianSpecialty) specialty).getName().toString();
            dto.setSpecialty(name);
        }

        return dto;
    }

    public Set<SpecialtyDto> mapToSpecialtyDtoSet(Set<Specialty> specialties) {
        Set<SpecialtyDto> specialtyDtos = specialties
                .stream()
                .map(this::mapToSpecialtyDto)
                .collect(Collectors.toSet());

        return specialtyDtos;
    }
}
