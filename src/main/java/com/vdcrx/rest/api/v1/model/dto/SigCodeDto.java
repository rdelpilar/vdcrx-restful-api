package com.vdcrx.rest.api.v1.model.dto;

import com.vdcrx.rest.domain.enums.SigCodeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * Sig code DTO
 *
 * @author Ranel del Pilar
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SigCodeDto {

    private UUID id;

    @NotNull(message = "{message.SigCodeDto.sigCodeType.null}")
    @Enumerated(EnumType.STRING)
    private SigCodeType sigCodeType;

    @NotBlank(message = "{message.SigCodeDto.definition.blank}")
    @Size(max = 64, message = "{message.SigCodeDto.definition.size}")
    private String definition;
}
