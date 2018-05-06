package com.vdcrx.rest.api.v1.mapper.entities;

import com.vdcrx.rest.api.v1.model.dto.ProfessionalSuffixDto;
import com.vdcrx.rest.domain.entities.ProfessionalSuffix;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Professional suffix to Dto mapper
 *
 * @author Ranel del Pilar
 */

@Mapper(componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProfessionalSuffixMapper {
    ProfessionalSuffixDto mapToProfessionalSuffixDto(final ProfessionalSuffix suffix);
    ProfessionalSuffix mapToProfessionalSuffix(final ProfessionalSuffixDto dto);
}
