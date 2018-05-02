package com.vdcrx.rest.api.v1.mapper.entities;

import com.vdcrx.rest.api.v1.model.dto.PhoneDto;
import com.vdcrx.rest.domain.entities.Phone;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

import static com.vdcrx.rest.utils.PhoneNumberUtil.stripWhitespaces;

/**
 * Phone to Dto mapper
 *
 * @author Ranel del Pilar
 */

@Mapper(componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class PhoneMapper {

    public abstract PhoneDto mapToPhoneDto(Phone phone);

    public Phone mapToPhone(PhoneDto dto) {
        return new Phone(dto.getPhoneType(), stripWhitespaces(dto.getPhone()), dto.getPhoneExt());
    }

    public abstract Set<PhoneDto> mapToPhoneDtoSet(Set<Phone> phones);
    public abstract Set<Phone> mapToPhoneSet(Set<PhoneDto> dtos);
}
