package com.vdcrx.rest.services.impl;

import com.vdcrx.rest.api.v1.model.dto.AddressDto;
import com.vdcrx.rest.api.v1.model.dto.PetOwnerDto;
import com.vdcrx.rest.services.PetOwnerService;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
public class PetOwnerServiceImpl implements PetOwnerService {
    @Override
    public PetOwnerDto createPetOwner(UUID vetId, PetOwnerDto resource) throws DataAccessException {
        return null;
    }

    @Override
    public PetOwnerDto updatePetOwner(UUID vetId, UUID petOwnerId, PetOwnerDto resource) throws DataAccessException {
        return null;
    }

    @Override
    public PetOwnerDto updatePetOwner(UUID petOwnerId, PetOwnerDto resource) throws DataAccessException {
        return null;
    }

    @Override
    public void deletePetOwner(UUID vetId, UUID petOwnerId) throws DataAccessException {

    }

    @Override
    public PetOwnerDto findPetOwnerById(UUID vetId, UUID petOwnerId) {
        return null;
    }

    @Override
    public PetOwnerDto findPetOwnerByUserName(String username) {
        return null;
    }

    @Override
    public PetOwnerDto findPetOwnerByEmail(String email) {
        return null;
    }

    @Override
    public List<PetOwnerDto> findPetOwnerByPhone(String phone) {
        return null;
    }

    @Override
    public List<PetOwnerDto> findByLastNameIsStartingWith(String prefix, int page, int size) {
        return null;
    }

    @Override
    public List<PetOwnerDto> findByFirstNameIsStartingWith(String firstName, int page, int size) {
        return null;
    }

    @Override
    public AddressDto createAddress(UUID id) {
        return null;
    }

    //
//    private final PetOwnerMapper petOwnerMapper;
//    private final PetOwnerRepository petOwnerRepository;
//
//    private final AddressMapper addressMapper;
//
//    @Autowired
//    public PetOwnerServiceImpl(PetOwnerMapper petOwnerMapper, PetOwnerRepository petOwnerRepository, AddressMapper addressMapper) {
//        this.petOwnerMapper = petOwnerMapper;
//        this.petOwnerRepository = petOwnerRepository;
//        this.addressMapper = addressMapper;
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public PetOwnerDto findPetOwnerByUserName(String username) throws DataAccessException {
//        return petOwnerMapper.petOwnerToPetOwnerDTO(petOwnerRepository.findPetOwnerByUserName(username));
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public PetOwnerDto findPetOwnerByEmail(String email) throws DataAccessException {
//        return petOwnerMapper.petOwnerToPetOwnerDTO(petOwnerRepository.findPetOwnerByEmail(email));
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public PetOwnerDto findPetOwnerByUuid(UUID id) throws DataAccessException {
//        return petOwnerMapper.petOwnerToPetOwnerDTO(petOwnerRepository.findPetOwnerByUuid(id));
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public Collection<AddressDto> findPetOwnerAddressesByUUID(UUID id) throws DataAccessException {
//
//        return null;
////        return petOwnerRepository
////                .findPetOwnerAddressesByUUID(id)
////                .stream()
////                .map(address -> {
////                    AddressDto addressDTO = addressMapper.AddressToAddressDTO(address);
////                    return addressDTO;
////                }).collect(Collectors.toList());
//    }
}
