package com.vdcrx.rest.api.v1.model.dto;

import com.vdcrx.rest.domain.enums.FacilityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * Facility DTO
 *
 * @author Ranel del Pilar
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacilityDto {

    private UUID id;

    @NotNull(message = "{message.FacilityDto.facilityType.null}")
    private FacilityType facilityType;

    @NotBlank(message = "{message.FacilityDto.name.blank}")
    @Size(max = 100, message = "{message.FacilityDto.name.size}")
    private String name;
}
