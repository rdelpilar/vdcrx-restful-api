package com.vdcrx.rest.api.v1.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * SpecialtyService DTO
 *
 * @author Ranel del Pilar
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecialtyDto {

    private UUID id;

    @NotBlank(message = "{message.SpecialtyDto.specialtyType.blank}")
    @Size(max = 50, message = "{message.SpecialtyDto.specialtyType.size}")
    private String specialty;
}
