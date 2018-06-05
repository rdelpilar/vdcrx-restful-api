package com.vdcrx.rest.services;

import com.vdcrx.rest.api.v1.model.dto.AddressDto;
import com.vdcrx.rest.api.v1.model.dto.PetOwnerDto;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.UUID;

/**
 * Pet owner service interface
 *
 * @author Ranel del Pilar
 */

public interface PetOwnerService {

    /* Create */
    PetOwnerDto createPetOwner(final UUID vetId, final PetOwnerDto resource) throws DataAccessException;

    /* Update from Veterinarian POV*/
    PetOwnerDto updatePetOwner(final UUID vetId, final UUID petOwnerId, final PetOwnerDto resource) throws DataAccessException;

    /* Update from Pet owner POV */
    PetOwnerDto updatePetOwner(final UUID petOwnerId, final PetOwnerDto resource) throws DataAccessException;

    /* Delete */
    void deletePetOwner(final UUID vetId, final UUID petOwnerId) throws DataAccessException;

    /* Retrieve */
    PetOwnerDto findPetOwnerById(final UUID vetId, final UUID petOwnerId);
    PetOwnerDto findPetOwnerByUserName(final String username);
    PetOwnerDto findPetOwnerByEmail(final String email);
    List<PetOwnerDto> findPetOwnerByPhone(final String phone);
    List<PetOwnerDto> findByLastNameIsStartingWith(final String prefix, final int page, final int size);
    List<PetOwnerDto> findByFirstNameIsStartingWith(final String firstName, final int page, final int size);

    /* Address */
    AddressDto createAddress(final UUID id);
}
