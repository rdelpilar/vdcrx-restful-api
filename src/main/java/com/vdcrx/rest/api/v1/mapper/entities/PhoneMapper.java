package com.vdcrx.rest.api.v1.mapper.entities;

import com.vdcrx.rest.api.v1.model.dto.PhoneDto;
import com.vdcrx.rest.domain.entities.Phone;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

/**
 * Phone to Dto mapper
 *
 * @author Ranel del Pilar
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhoneMapper {
    PhoneDto mapToPhoneDto(Phone phone);
    Phone mapToPhone(PhoneDto dto);

    Set<PhoneDto> mapToPhoneDtoSet(Set<Phone> phones);
    Set<Phone> mapToPhoneSet(Set<PhoneDto> dtos);

    Set<PhoneDto> mapListToPhoneDtoSet(List<Phone> phones);
    Set<Phone> mapListToPhoneSet(List<PhoneDto> phones);
}
