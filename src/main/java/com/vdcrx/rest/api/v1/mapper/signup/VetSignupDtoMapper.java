package com.vdcrx.rest.api.v1.mapper.signup;

import com.vdcrx.rest.api.v1.model.dto.signup.*;
import com.vdcrx.rest.api.v1.model.dto.signup.veterinarian.VetSignupDto;
import com.vdcrx.rest.domain.entities.*;
import com.vdcrx.rest.domain.enums.VetSpecialtyType;
import org.mapstruct.*;

import static com.vdcrx.rest.utils.PhoneNumberUtil.stripWhitespaces;

@Mapper(componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class VetSignupDtoMapper {

    @Mappings({
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "firstName", source = "firstName"),
            @Mapping(target = "middleName", source = "middleName"),
            @Mapping(target = "lastName", source = "lastName"),
            @Mapping(target = "email", source = "email"),
            @Mapping(target = "phones", source = "phones"),
            @Mapping(target = "addresses", source = "addresses"),
            @Mapping(target = "facilities", source = "facilities"),
            @Mapping(target = "specialties", source = "specialties")
    })
    public abstract Veterinarian mapToVeterinarian(VetSignupDto signupDto);

    Specialty maptToSpecialty(SignupSpecialtyDto dto) {
        return new VeterinarianSpecialty(VetSpecialtyType.valueOf(dto.getSpecialty()));
    }

    Facility mapToFacility(SignupFacilityDto dto) {
        return new VeterinarianFacility(dto.getFacilityType(), dto.getName());
    }

    Phone mapToPhone(SignupPhoneDto dto) {
        return new Phone(dto.getPhoneType(), stripWhitespaces(dto.getPhone()), dto.getPhoneExt());
    }

    public abstract Address mapToAddress(SignupAddressDto addressDto);

    public abstract ProviderId mapToProviderId(SignupProviderIdDto providerIdDto);

    public abstract Signature mapToSignature(SignupSignatureDto signatureDto);
}
