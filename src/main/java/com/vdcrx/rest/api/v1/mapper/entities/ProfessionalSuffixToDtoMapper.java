package com.vdcrx.rest.api.v1.mapper.entities;

import com.vdcrx.rest.api.v1.model.dto.ProfessionalSuffixDto;
import com.vdcrx.rest.domain.entities.ProfessionalSuffix;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

/**
 * Professional suffix to Dto mapper
 *
 * @author Ranel del Pilar
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProfessionalSuffixToDtoMapper {
    ProfessionalSuffixDto mapToProfessionalSuffixDto(ProfessionalSuffix professionalSuffix);
    Set<ProfessionalSuffixDto> mapToProfessionalSuffixDtoSet(Set<ProfessionalSuffix> suffixes);
    Set<ProfessionalSuffixDto> mapListToProfessionalSuffixDtoSet(List<ProfessionalSuffix> suffixes);
}
