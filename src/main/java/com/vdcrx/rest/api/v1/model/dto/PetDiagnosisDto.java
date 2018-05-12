package com.vdcrx.rest.api.v1.model.dto;

import com.vdcrx.rest.domain.enums.PetDiagnosisType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * Pet diagnosis DTO
 *
 * @author Ranel del Pilar
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PetDiagnosisDto {

    private UUID id;

    @NotNull(message = "{message.PetDiagnosisDto.petDiagnosisType.null}")
    @Enumerated(value = EnumType.STRING)
    private PetDiagnosisType petDiagnosisType;

    @NotNull(message = "{message.PetDiagnosisDto.definition.null}")
    @Size(max = 100, message = "{message.PetDiagnosisDto.definition.size}")
    private String definition;
}
