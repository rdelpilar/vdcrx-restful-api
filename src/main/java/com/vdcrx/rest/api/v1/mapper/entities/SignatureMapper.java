package com.vdcrx.rest.api.v1.mapper.entities;

import com.vdcrx.rest.api.v1.model.dto.SignatureDto;
import com.vdcrx.rest.domain.entities.Signature;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Signature to Dto mapper
 *
 * @author Ranel del Pilar
 */

@Mapper(componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SignatureMapper {

    SignatureDto mapToSignatureDto(final Signature signature);
    Signature mapToSignature(final SignatureDto dto);
}
