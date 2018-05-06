package com.vdcrx.rest.api.v1.model.dto;

import com.vdcrx.rest.validators.ValidName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * Person basic info DTO
 *
 * @author Ranel del Pilar
 */

@Getter
@Setter
@NoArgsConstructor
public class PersonBasicDto {

    private UUID id;

    @NotBlank(message = "{message.PersonBasicDto.blank.firstName.blank}")
    @ValidName(message = "{message.PersonBasicDto.firstName.validName}")
    private String firstName;

    @Size(max = 32, message = "{message.PersonBasicDto.middleName.size}")
    private String middleName;

    @NotBlank(message = "{message.PersonBasicDto.lastName.blank}")
    @ValidName(message = "{message.PersonBasicDto.lastName.validName}")
    private String lastName;
}
