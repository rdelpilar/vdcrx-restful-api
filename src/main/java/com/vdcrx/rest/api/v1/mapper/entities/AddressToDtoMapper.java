package com.vdcrx.rest.api.v1.mapper.entities;

import com.vdcrx.rest.api.v1.model.dto.AddressDto;
import com.vdcrx.rest.domain.entities.Address;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

/**
 * Address to Dto mapper
 *
 * @author Ranel del Pilar
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressToDtoMapper {
    AddressDto mapToAddressDto(Address address);
    Set<AddressDto> mapToAddressDtoSet(Set<Address> addresses);
    Set<AddressDto> mapListToAddressDtoSet(List<Address> addresses);
}
