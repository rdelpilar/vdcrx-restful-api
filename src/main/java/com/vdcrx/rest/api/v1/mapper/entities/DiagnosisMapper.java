package com.vdcrx.rest.api.v1.mapper.entities;

/**
 * Diagnosis mapper
 *
 * @author Ranel del Pilar
 */

import com.vdcrx.rest.api.v1.model.dto.PetDiagnosisDto;
import com.vdcrx.rest.domain.entities.Diagnosis;
import com.vdcrx.rest.domain.entities.PetDiagnosis;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class DiagnosisMapper {
    PetDiagnosisDto mapToPetDiagnosisDto(final Diagnosis diagnosis) {

        PetDiagnosisDto dto = new PetDiagnosisDto();

        if(diagnosis instanceof PetDiagnosis) {
            dto.setPetDiagnosisType(((PetDiagnosis) diagnosis).getPetDiagnosisType());
            dto.setDefinition(((PetDiagnosis) diagnosis).getDefinition());
            dto.setId(diagnosis.getId());
        }

        return dto;
    }

    public abstract PetDiagnosis mapToPetDiagnosis(final PetDiagnosisDto dto);
}
