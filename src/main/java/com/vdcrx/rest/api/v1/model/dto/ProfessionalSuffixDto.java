package com.vdcrx.rest.api.v1.model.dto;

import com.vdcrx.rest.domain.enums.ProfessionalSuffixType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Professional suffix DTO
 *
 * @author Ranel del Pilar
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalSuffixDto {

    private UUID id;

    @NotNull(message = "{message.ProfessionalSuffixDto.professionalSuffixType.null}")
    private ProfessionalSuffixType professionalSuffixType;
}
