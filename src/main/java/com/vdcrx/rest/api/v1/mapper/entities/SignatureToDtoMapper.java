package com.vdcrx.rest.api.v1.mapper.entities;

import com.vdcrx.rest.api.v1.model.dto.SignatureDto;
import com.vdcrx.rest.domain.entities.Signature;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Signature to Dto mapper
 *
 * @author Ranel del Pilar
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SignatureToDtoMapper {
    SignatureDto mapToSignatureDto(Signature signature);
}
