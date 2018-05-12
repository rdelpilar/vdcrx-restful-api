package com.vdcrx.rest.api.v1.model.dto;

import com.vdcrx.rest.domain.enums.PetAllergyType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Pet allergy DTO
 *
 * @author Ranel del Pilar
 */

@Getter
@Setter
@NoArgsConstructor
public class PetAllergyDto {

    private UUID id;

    @NotNull(message = "{message.PetAllergyDto.petAllergyType.null}")
    private PetAllergyType petAllergyType = PetAllergyType.NONE;
}
