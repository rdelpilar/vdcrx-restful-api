package com.vdcrx.rest.api.v1.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Veterinarian DTO
 *
 * @author Ranel del Pilar
 */

@Getter
@Setter
@NoArgsConstructor
public class VetDto extends PersonDto {

    @Valid
    @NotNull(message = "{message.VetDto.suffixes.null}")
    private Set<ProfessionalSuffixDto> suffixes;

    @Valid
    @NotNull(message = "{message.VetDto.facilities.null}")
    private Set<FacilityDto> facilities;

    @Valid
    @NotNull(message = "{message.VetDto.specialties.null}")
    private Set<SpecialtyDto> specialties;

    @Valid
    private ProviderIdDto providerId;

    @Valid
    @NotNull(message = "{message.VetDto.signature.null}")
    private SignatureDto signature;
}
