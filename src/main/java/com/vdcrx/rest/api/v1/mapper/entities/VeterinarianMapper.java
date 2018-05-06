package com.vdcrx.rest.api.v1.mapper.entities;

import com.vdcrx.rest.api.v1.model.dto.PersonBasicDto;
import com.vdcrx.rest.api.v1.model.dto.VetDto;
import com.vdcrx.rest.domain.entities.Veterinarian;
import org.mapstruct.*;

/**
 * Veterinarian mapper
 *
 * @author Ranel del Pilar
 */

@Mapper(componentModel = "spring",
        uses = {AddressMapper.class,
                FacilityMapper.class,
                PhoneMapper.class,
                ProfessionalSuffixMapper.class,
                SpecialtyMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class VeterinarianMapper {

    @Mappings({
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "firstName", source = "firstName"),
            @Mapping(target = "middleName", source = "middleName"),
            @Mapping(target = "lastName", source = "lastName"),
            @Mapping(target = "suffixes", source = "suffixes"),
            @Mapping(target = "email", source = "email"),
            @Mapping(target = "phones", source = "phones"),
            @Mapping(target = "addresses", source = "addresses"),
            @Mapping(target = "facilities", source = "facilities"),
            @Mapping(target = "specialties", source = "specialties")
    })
    public abstract Veterinarian mapToVeterinarian(VetDto signupDto);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "firstName", source = "firstName"),
            @Mapping(target = "middleName", source = "middleName"),
            @Mapping(target = "lastName", source = "lastName"),
            @Mapping(target = "email", source = "email"),
            @Mapping(target = "phones", source = "phones"),
            @Mapping(target = "suffixes", source = "suffixes"),
            @Mapping(target = "addresses", source = "addresses"),
            @Mapping(target = "facilities", source = "facilities"),
            @Mapping(target = "specialties", source = "specialties")
    })
    public abstract VetDto mapToVetDto(Veterinarian veterinarian);

    public abstract PersonBasicDto mapToPersonBasicDto(Veterinarian veterinarian);
}
