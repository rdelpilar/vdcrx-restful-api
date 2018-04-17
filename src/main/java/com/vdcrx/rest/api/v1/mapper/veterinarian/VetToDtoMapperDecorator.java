//package com.vdcrx.rest.api.v1.mapper.veterinarian;
//
//import com.vdcrx.rest.api.v1.model.dto.ProviderIdDto;
//import com.vdcrx.rest.api.v1.model.dto.veterinarian.VetDto;
//import com.vdcrx.rest.domain.entities.Veterinarian;
//import com.vdcrx.rest.services.ProviderIdService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//
//import java.util.Set;
//
///**
// * Veterinarian to DTO mapper decorator
// *
// * @author Ranel del Pilar
// */
//
//public abstract class VetToDtoMapperDecorator implements VetToDtoMapper {
//
//    @Autowired
//    @Qualifier("delegate")
//    private VetToDtoMapper delegate;
//
//    @Autowired
//    private ProviderIdService providerIdService;
//
//    @Override
//    public VetDto mapToVetDto(Veterinarian veterinarian) {
//        VetDto vetDto = delegate.mapToVetDto(veterinarian);
//
//        ProviderIdDto providerIdDto = providerIdService.findProviderIdByUuid(vetDto.getUuid());
//        vetDto.setProviderIdDto(providerIdDto);
//
//        return vetDto;
//    }
//
//    @Override
//    public Set<VetDto> mapToVetDtoSet(Set<Veterinarian> veterinarians) {
//        return null;
//    }
//}
