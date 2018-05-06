package com.vdcrx.rest.api.v1.model.dto;

import com.vdcrx.rest.validators.ValidPassword;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Password DTO
 *
 * @author Ranel del Pilar
 */

@Getter
@Setter
@NoArgsConstructor
public class PasswordDto {

    private String current;

    @ValidPassword
    private String replacement;
}
