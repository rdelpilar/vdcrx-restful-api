package com.vdcrx.rest.api.v1.model.dto;

import com.vdcrx.rest.domain.enums.PetGenderType;
import com.vdcrx.rest.domain.enums.SpeciesType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Lob;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * Pet DTO
 *
 * @author Ranel del Pilar
 */

@Getter
@Setter
@NoArgsConstructor
public class PetDto {

    @NotBlank(message = "{message.PetDto.name.blank}")
    @Size(min = 1, max = 32, message = "{message.PetDto.name.size}")
    private String name;

    @NotNull(message = "{message.PetDto.speciesType.null}")
    private SpeciesType speciesType = SpeciesType.OTHERS;

    @NotNull(message = "{message.PetDto.petGenderType.null}")
    private PetGenderType petGenderType = PetGenderType.MALE;

    private long dateOfBirth;

    private double weight;

    @NotBlank(message = "{message.PetDto.diseaseState.blank}")
    @Size(min = 5, max = 32, message = "{message.PetDto.diseaseState.size}")
    private String diseaseState;

    @Valid
    private Set<PetAllergyDto> allergies;

    @Valid
    private Set<PetMedicationDto> medications;

    @Lob
    private byte [] photo;
}
