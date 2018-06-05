package com.vdcrx.rest.services;

import com.vdcrx.rest.api.v1.model.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.UUID;

/**
 * Veterinarian service interface
 *
 * @author Ranel del Pilar
 */

public interface VeterinarianService {

    /* Create veterinarian */
    VetDto createVeterinarian(final VetDto dto);

    /* Update Veterinarian */
    PersonBasicDto updateVeterinarianBasicInfo(final UUID id, final PersonBasicDto resource);

    /* Retrieve veterinarian */
    VetDto findOne(final UUID id);
    VetDto findById(final UUID id);
    VetDto findByUsername(final String username);
    VetDto findByEmail(final String email);
    List<VetDto> findByPhone(final String phone);
    VetDto findByDEA(final String dea);
    VetDto findByME(final String me);
    VetDto findByNPI(final String npi);
    List<VetDto> findByLastNameIsStartingWith(final String prefix, final int page, final int size);
    List<VetDto> findByFirstNameIsStartingWith(final String firstName, final int page, final int size);

    /* Address */
    AddressDto createAddress(final UUID id, final AddressDto resource);
    AddressDto updateAddress(final UUID id, final UUID address_id, final AddressDto resource);
    void deleteAddress(final UUID id, final UUID address_id);
    AddressDto findAddressById(final UUID id, final UUID address_id);
    List<AddressDto> findAddressesByPersonId(final UUID id);

    /* Phone */
    PhoneDto createPhone(final UUID id, final PhoneDto resource);
    PhoneDto updatePhone(final UUID id, final UUID phone_id, final PhoneDto resource);
    void deletePhone(final UUID id, final UUID phone_id);
    PhoneDto findPhoneById(final UUID id, final UUID phone_id);
    List<PhoneDto> findPhonesByPersonId(final UUID id);

    /* Provider Identifier */
    ProviderIdDto createProviderIdentifier(final UUID id, final ProviderIdDto resource);
    ProviderIdDto updateProviderIdentifier(final UUID id, final UUID provider_id, final ProviderIdDto resource);
    void deleteProviderIdentifier(final UUID id, final UUID provider_id);
    ProviderIdDto findProviderIdById(final UUID id, final UUID provider_id);
    ProviderIdDto findProviderIdByPersonId(final UUID id);

    /* Facility */
    FacilityDto createFacility(final UUID id, final FacilityDto resource);
    FacilityDto updateFacility(final UUID id, final UUID facility_id, final FacilityDto resource);
    void deleteFacility(final UUID id, final UUID facility_id);
    FacilityDto findFacilityById(final UUID id, final UUID facility_id);
    List<FacilityDto> findFacilitiesByPersonId(final UUID id);

    /* Specialty */
    SpecialtyDto createSpecialty(final UUID id, final SpecialtyDto resource);
    SpecialtyDto updateSpecialty(final UUID id, final UUID specialty_id, final SpecialtyDto resource);
    void deleteSpecialty(final UUID id, final UUID specialty_id);
    SpecialtyDto findSpecialtyById(final UUID id, final UUID specialty_id);
    List<SpecialtyDto> findSpecialtiesByPersonId(final UUID id);

    /* Professional Suffix */
    ProfessionalSuffixDto createProfessionalSuffix(final UUID id, final ProfessionalSuffixDto resource);
    ProfessionalSuffixDto updateProfessionalSuffix(final UUID id, final UUID suffix_id, final ProfessionalSuffixDto resource);
    void deleteProfessionalSuffix(final UUID id, final UUID suffix_id);
    ProfessionalSuffixDto findProfessionalSuffixById(final UUID id, final UUID suffix_id);
    List<ProfessionalSuffixDto> findProfessionalSuffixesByPersonId(final UUID id);

    /* Signature */
    SignatureDto createSignature(final UUID id, final SignatureDto resource);
    SignatureDto updateSignature(final UUID id, final UUID sig_id, final SignatureDto resource);
    void deleteSignature(final UUID id, final UUID sig_id);
    SignatureDto findSignatureById(final UUID id, final UUID sig_id);
    SignatureDto findSignatureByPersonId(final UUID id);

    /* Pet Owner*/
    PetOwnerDto createPetOwner(final UUID id, final PetOwnerDto resource);
    PetOwnerDto findPetOwnerById(final UUID id, final UUID petOwnerId);

    // TODO: Below are temporary
    List<VetDto> getAll();
    Page<VetDto> findPaginated(final int page, final int size);
}
