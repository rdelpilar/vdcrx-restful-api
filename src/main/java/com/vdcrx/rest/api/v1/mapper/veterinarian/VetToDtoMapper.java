package com.vdcrx.rest.api.v1.mapper.veterinarian;

import com.vdcrx.rest.api.v1.mapper.entities.SpecialtyToDtoMapper;
import com.vdcrx.rest.api.v1.model.dto.veterinarian.VetDto;
import com.vdcrx.rest.domain.entities.Veterinarian;
import org.mapstruct.*;

import java.util.Set;

/**
 * Veterinarian to DTO mapper
 *
 * @author Ranel del Pilar
 */

@Mapper(componentModel = "spring",
        uses = {SpecialtyToDtoMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VetToDtoMapper {

    @Mappings({
            @Mapping(target = "uuid", source = "uuid"),
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "firstName", source = "firstName"),
            @Mapping(target = "middleName", source = "middleName"),
            @Mapping(target = "lastName", source = "lastName"),
            @Mapping(target = "email", source = "email"),
            @Mapping(target = "phones", source = "phones"),
            @Mapping(target = "addresses", source = "addresses"),
            @Mapping(target = "facilities", source = "facilities"),
            @Mapping(target = "specialties", source = "specialties"),
    })
    VetDto mapToVetDto(Veterinarian veterinarian);

    Set<VetDto> mapToVetDtoSet(Set<Veterinarian> veterinarians);
}
