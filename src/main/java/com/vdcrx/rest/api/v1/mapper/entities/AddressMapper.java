package com.vdcrx.rest.api.v1.mapper.entities;

import com.vdcrx.rest.api.v1.model.dto.AddressDto;
import com.vdcrx.rest.domain.entities.Address;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

/**
 * Address to Dto mapper
 *
 * @author Ranel del Pilar
 */

@Mapper(componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {
    AddressDto mapToAddressDto(final Address address);
    Address mapToAddress(final AddressDto dto);

    Set<AddressDto> mapToAddressDtoSet(final Set<Address> addresses);
    Set<Address> mapToAddressSet(final Set<AddressDto> addressDtos);

    Set<AddressDto> mapListToAddressDtoSet(final List<Address> addresses);
    Set<AddressDto> mapListToAddressSet(final List<AddressDto> addressDtos);
}
