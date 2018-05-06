package com.vdcrx.rest.api.v1.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Address collection DTO
 *
 * @author Ranel del Pilar
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddressCollectionDto {
    private Set<AddressDto> addresses;
}
