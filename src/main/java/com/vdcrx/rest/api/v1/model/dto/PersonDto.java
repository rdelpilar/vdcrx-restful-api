package com.vdcrx.rest.api.v1.model.dto;

import com.vdcrx.rest.validators.ValidEmail;
import com.vdcrx.rest.validators.ValidName;
import com.vdcrx.rest.validators.ValidPassword;
import com.vdcrx.rest.validators.ValidUsername;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.UUID;

/**
 * Abstract Person DTO
 *
 * @author Ranel del Pilar
 */

@Getter
@Setter
@NoArgsConstructor
public abstract class PersonDto {

    private UUID id;

    @NotBlank(message = "{message.PersonDto.username.blank}")
    @ValidUsername(message = "{message.PersonDto.validUsername}")
    private String username;

    @NotBlank(message = "{message.PersonDto.password.blank}")
    @ValidPassword
    private String plainTextPassword;

    @NotBlank(message = "{message.PersonDto.blank.firstName.blank}")
    @ValidName(message = "{message.PersonDto.firstName.validName}")
    private String firstName;

    @Size(max = 32, message = "{message.PersonDto.middleName.size}")
    private String middleName;

    @NotBlank(message = "{message.PersonDto.lastName.blank}")
    @ValidName(message = "{message.PersonDto.lastName.validName}")
    private String lastName;

    @NotBlank(message = "{message.PersonDto.email.blank}")
    @ValidEmail(message = "{message.PersonDto.email.validFormat}")
    private String email;

    @Valid
    @NotNull(message = "{message.PersonDto.phones.null}")
    private Set<PhoneDto> phones;

    @Valid
    @NotNull(message = "{message.PersonDto.addresses.null}")
    private Set<AddressDto> addresses;
}
