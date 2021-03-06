package com.vdcrx.rest.api.v1.mapper.entities;

import com.vdcrx.rest.api.v1.model.dto.AddressDto;
import com.vdcrx.rest.domain.entities.Address;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

/**
 * Address mapper
 *
 * @author Ranel del Pilar
 */

@Mapper(componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {
    AddressDto mapToAddressDto(final Address address);
    Address mapToAddress(final AddressDto dto);
    List<AddressDto> mapToAddressDtoList(final Set<Address> addresses);
}
