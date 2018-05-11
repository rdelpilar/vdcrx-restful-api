package com.vdcrx.rest.api.v1.mapper.entities;

import com.vdcrx.rest.api.v1.model.dto.PetMedicationDto;
import com.vdcrx.rest.domain.entities.Medication;
import com.vdcrx.rest.domain.entities.PetMedication;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Sig code mapper
 *
 * @author Ranel del Pilar
 */

@Mapper(componentModel = "spring",
        uses = {SigCodeMapper.class, DiagnosisMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MedicationMapper {
    public abstract PetMedicationDto mapToPetMedicationDto(final Medication medication);
    public abstract PetMedication mapToPetMedication(final PetMedicationDto dto);
}
