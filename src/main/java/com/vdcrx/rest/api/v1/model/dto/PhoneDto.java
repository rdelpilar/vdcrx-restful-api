package com.vdcrx.rest.api.v1.model.dto;

import com.vdcrx.rest.domain.enums.PhoneType;
import com.vdcrx.rest.validators.ValidPhone;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * Phone DTO
 *
 * @author Ranel del Pilar
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhoneDto {

    private UUID id;

    @NotNull(message = "{message.PhoneDto.phoneType.null}")
    private PhoneType phoneType = PhoneType.MOBILE;

    @NotBlank(message = "{message.PhoneDto.phone.blank}")
    @ValidPhone(message = "{message.PhoneDto.phone.validPhone}")
    @Size(max = 16, message = "{message.PhoneDto.phone.size}")
    private String phone;

    @Size(max = 6, message = "{message.PhoneDto.phoneExt.size}")
    private String phoneExt;
}
