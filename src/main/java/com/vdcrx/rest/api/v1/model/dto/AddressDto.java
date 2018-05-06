package com.vdcrx.rest.api.v1.model.dto;

import com.vdcrx.rest.domain.enums.AddressType;
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
 * Address DTO
 *
 * @author Ranel del Pilar
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    private UUID id;

    @NotBlank(message = "{message.AddressDto.address1.blank}")
    @Size(min = 3, max = 64, message = "{message.AddressDto.address1.size}")
    private String address1;

    @Size(max = 64, message = "{message.AddressDto.address2.size}")
    private String address2;

    @NotBlank(message = "{message.AddressDto.city.blank}")
    @Size(min = 3, max = 50, message = "{message.AddressDto.city.size}")
    private String city;

    @NotBlank(message = "{message.AddressDto.state.blank}")
    @Size(min = 2, max = 50, message = "{message.AddressDto.state.size}")
    private String state;

    @NotBlank(message = "{message.AddressDto.postalCode.blank}")
    @Size(min = 5, max = 10, message = "{message.AddressDto.postalCode.size}")
    private String postalCode;

    @NotNull(message = "{message.AddressDto.addressType.null}")
    @Enumerated(EnumType.STRING)
    private AddressType addressType;
}
