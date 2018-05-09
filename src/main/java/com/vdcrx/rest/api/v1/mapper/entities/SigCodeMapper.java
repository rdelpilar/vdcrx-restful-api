package com.vdcrx.rest.api.v1.mapper.entities;

import com.vdcrx.rest.api.v1.model.dto.SigCodeDto;
import com.vdcrx.rest.domain.entities.SigCode;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Sig code mapper
 *
 * @author Ranel del Pilar
 */

@Mapper(componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SigCodeMapper {
    SigCode mapToSigCode(SigCodeDto dto);
    SigCodeDto mapToSigCodeDto(SigCode sigCode);
}
