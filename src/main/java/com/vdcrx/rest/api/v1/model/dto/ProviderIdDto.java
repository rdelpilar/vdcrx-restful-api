package com.vdcrx.rest.api.v1.model.dto;

import com.vdcrx.rest.domain.enums.ProviderIdType;
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
 * Provider identifier DTO
 *
 * @author Ranel del Pilar
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProviderIdDto {

    private UUID id;

    @NotBlank(message = "{message.ProviderIdDto.dea.blank}")
    @Size(min = 10, max = 10, message = "{message.ProviderIdDto.dea.size}")
    private String dea;

    @NotBlank(message = "{message.ProviderIdDto.me.blank}")
    @Size(min = 11, max = 11, message = "{message.ProviderIdDto.me.size}")
    private String me;

    @NotBlank(message = "{message.ProviderIdDto.npi.blank}")
    @Size(min = 10, max = 10, message = "{message.ProviderIdDto.npi.size}")
    private String npi;

    @NotNull(message = "{message.ProviderIdDto.ProviderIdType.null}")
    @Enumerated(EnumType.STRING)
    private ProviderIdType providerIdType;
}
