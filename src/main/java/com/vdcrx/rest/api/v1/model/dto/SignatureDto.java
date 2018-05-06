package com.vdcrx.rest.api.v1.model.dto;

import com.vdcrx.rest.domain.enums.SignatureType;
import com.vdcrx.rest.domain.interfaces.ISignature;
import com.vdcrx.rest.validators.ValidSignature;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Lob;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

import static com.vdcrx.rest.domain.entities.Signature.MAX_SIG_SIZE;

/**
 * Signature DTO
 *
 * @author Ranel del Pilar
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ValidSignature
public class SignatureDto implements ISignature {

    private UUID id;

    @NotNull(message = "{message.SignatureDto.signatureType.null}")
    private SignatureType signatureType;

    @Max(value = MAX_SIG_SIZE, message = "{message.SignatureDto.size.size}")
    private int size;

    @NotBlank(message = "{message.SignatureDto.contentType.blank}")
    @Size(max = 32, message = "{message.SignatureDto.contentType.size}")
    private String contentType;

    @Lob
    private byte[] image;
}
