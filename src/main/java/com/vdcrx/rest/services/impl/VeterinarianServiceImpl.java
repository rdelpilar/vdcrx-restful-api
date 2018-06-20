package com.vdcrx.rest.services.impl;

import com.vdcrx.rest.api.v1.mapper.entities.VeterinarianMapper;
import com.vdcrx.rest.api.v1.model.dto.*;
import com.vdcrx.rest.domain.entities.Role;
import com.vdcrx.rest.domain.entities.Veterinarian;
import com.vdcrx.rest.domain.enums.RoleType;
import com.vdcrx.rest.exceptions.ResourceNotFoundException;
import com.vdcrx.rest.repositories.VeterinarianRepository;
import com.vdcrx.rest.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.vdcrx.rest.constants.ApiConstants.VET_BASE_PATH;
import static com.vdcrx.rest.utils.PhoneNumberUtil.stripWhitespaces;

/**
 * Veterinarian service implementation
 *
 * @author Ranel del Pilar
 */

@Service
@VeterinarianRole
public class VeterinarianServiceImpl implements VeterinarianService {

    private static final Logger LOG = LoggerFactory.getLogger(VeterinarianServiceImpl.class);

    private VeterinarianMapper vetMapper;
    private VeterinarianRepository veterinarianRepository;
    private AddressService addressService;
    private PhoneService phoneService;
    private ProviderIdService providerIdService;
    private FacilityService facilityService;
    private SpecialtyService specialtyService;
    private ProfessionalSuffixService professionalSuffixService;
    private SignatureService signatureService;
    private PasswordService passwordService;
    private PetOwnerService petOwnerService;

    @Autowired
    public VeterinarianServiceImpl(VeterinarianMapper vetMapper,
                                   VeterinarianRepository veterinarianRepository,
                                   AddressService addressService,
                                   PhoneService phoneService,
                                   FacilityService facilityService,
                                   SpecialtyService specialtyService,
                                   ProfessionalSuffixService professionalSuffixService,
                                   SignatureService signatureService,
                                   ProviderIdService providerIdService,
                                   PasswordService passwordService,
                                   PetOwnerService petOwnerService) {
        this.vetMapper = vetMapper;
        this.veterinarianRepository = veterinarianRepository;
        this.addressService = addressService;
        this.phoneService = phoneService;
        this.facilityService = facilityService;
        this.specialtyService = specialtyService;
        this.professionalSuffixService = professionalSuffixService;
        this.signatureService = signatureService;
        this.providerIdService = providerIdService;
        this.passwordService = passwordService;
        this.petOwnerService = petOwnerService;
    }

    private String getVeterinarianUrl(UUID id) {
        return VET_BASE_PATH + "/" + id.toString();
    }

    @Override
    @Transactional
    public VetDto createVeterinarian(final VetDto dto) {
        Assert.notNull(dto, "Registration Dto must not be null!");

        // Create veterinarian
        Veterinarian vet = vetMapper.mapToVeterinarian(dto);
        vet.setLoginEnabled(true);
        vet.setAccessFailedCount(0);
        vet.setLastPasswordResetDate(System.currentTimeMillis());

        // Encode password
        vet.setHashedPassword(passwordService.encodePassword(dto.getPlainTextPassword()));

        // Create role(s)
        vet.addRole(new Role(RoleType.VETERINARIAN));

        // Persist veterinarian
        LOG.info("Persisting veterinarian account '" + vet.getUsername() + "'");
        Veterinarian vetRet = veterinarianRepository.saveAndFlush(vet);

        // Then create following entities:

        // Create signature
        SignatureDto retSignatureDto = createSignature(vetRet, dto);

        // Create provider identifier
        ProviderIdDto retProviderIdDto = createProviderId(vetRet, dto);

        VetDto retVetDto = vetMapper.mapToVetDto(vetRet);

        retVetDto.setSignature(retSignatureDto);
        retVetDto.setProviderId(retProviderIdDto);

        return retVetDto;
    }

    private SignatureDto createSignature(Veterinarian vet, VetDto dto) {
        return signatureService.createSignature(vet.getId(), dto.getSignature());
    }

    private ProviderIdDto createProviderId(Veterinarian vet, VetDto dto) {
        return providerIdService.createProviderIdentifier(vet.getId(), dto.getProviderId());
    }

    // Remove this. Use pagination when returning large numbers of objects to client
    @Override
    @Transactional(readOnly = true)
    //@Cacheable(value = "veterinarians")
    public List<VetDto> getAll() {

        LOG.debug("Retrieving all Veterinarian entities");

        return veterinarianRepository
                .findAll()
                .stream()
                .map(this::decorateVeterinarianDto)
                .collect(Collectors.toList());
    }

    // Temporary
    @Override
    @Transactional(readOnly = true)
    public Page<VetDto> findPaginated(final int page, final int size) {
        return veterinarianRepository.findAll(PageRequest.of(page, size, Sort.Direction.ASC, "firstName"))
                .map(this::decorateVeterinarianDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VetDto> findByLastNameIsStartingWith(final String prefix, final int page, final int size) {
        Assert.notNull(prefix, "First name must not be null!");

        LOG.debug("Retrieving Veterinarian with last name starting with'" + prefix + "'");

        return veterinarianRepository.findVeterinariansByLastNameIsStartingWith(prefix,
                PageRequest.of(
                        page,
                        size,
                        Sort.Direction.ASC,
                        "lastName",
                        "firstName"))
                .stream()
                .map(this::decorateVeterinarianDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VetDto> findByFirstNameIsStartingWith(final String prefix, final int page, final int size) {
        Assert.notNull(prefix, "First name must not be null!");

        LOG.debug("Retrieving Veterinarian with first name starting with'" + prefix + "'");

        return veterinarianRepository.findVeterinariansByFirstNameIsStartingWith(prefix,
                PageRequest.of(
                        page,
                        size,
                        Sort.Direction.ASC,
                        "lastName",
                        "firstName"))
                .stream()
                .map(this::decorateVeterinarianDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public VetDto findOne(final UUID id) {
        Assert.notNull(id, "Id must not be null!");

        LOG.debug("findOne - Retrieving Veterinarian with id '" + id + "'");

        Optional<Veterinarian> ret = Optional.of(veterinarianRepository.getOne(id));

        return ret.map(this::decorateVeterinarianDto)
                .orElseThrow(() -> new ResourceNotFoundException("No Veterinarian found!"));
    }

    @Override
    @Transactional(readOnly = true)
    public VetDto findById(final UUID id) {
        Assert.notNull(id, "Id must not be null!");

        LOG.debug("Retrieving veterinarian with id '" + id + "'");

        return veterinarianRepository.findVeterinarianById(id)
                .map(this::decorateVeterinarianDto)
                .orElseThrow(() -> new ResourceNotFoundException("No Veterinarian found!"));
    }

    @Override
    @Transactional(readOnly = true)
    public VetDto findByUsername(final String username) {
        Assert.notNull(username, "Username must not be null!");

        LOG.debug("Retrieving veterinarian with username '" + username + "'");

        return veterinarianRepository.findVeterinarianByUsername(username)
                .map(this::decorateVeterinarianDto)
                .orElseThrow(() -> new ResourceNotFoundException("No veterinarian found with username '" + username + "'"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VetDto> findByPhone(final String phone) {
        Assert.notNull(phone, "Phone must not be null!");

        LOG.debug("Retrieving veterinarians with phone '" + phone + "'");

        String parsed = stripWhitespaces(phone);

        List<Veterinarian> list = veterinarianRepository.findVeterinariansByPhone(parsed);

        if(list.isEmpty())
            throw new ResourceNotFoundException("No veterinarian found with phone '" + phone + "'");

        return list.stream().map(this::decorateVeterinarianDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public VetDto findByEmail(final String email) {
        Assert.notNull(email, "Email must not be null!");

        LOG.debug("Retrieving Veterinarian with email '" + email + "'");

        return veterinarianRepository.findVeterinarianByEmail(email)
                .map(this::decorateVeterinarianDto)
                .orElseThrow(() -> new ResourceNotFoundException("No veterinarian found with email '" + email + "'"));
    }

    @Override
    @Transactional(readOnly = true)
    public VetDto findByDEA(final String dea) {
        Assert.notNull(dea, "DEA must not be null!");

        LOG.debug("Retrieving Veterinarian with DEA '" + dea + "'");

        return veterinarianRepository.findVeterinarianByDEA(dea)
                .map(this::decorateVeterinarianDto)
                .orElseThrow(() -> new ResourceNotFoundException("No veterinarian found with dea '" + dea + "'"));
    }

    @Override
    @Transactional(readOnly = true)
    public VetDto findByME(final String me) {
        Assert.notNull(me, "ME must not be null!");

        LOG.debug("Retrieving Veterinarian with ME '" + me + "'");

        return veterinarianRepository.findVeterinarianByME(me)
                .map(this::decorateVeterinarianDto)
                .orElseThrow(() -> new ResourceNotFoundException("No veterinarian found with me '" + me + "'"));
    }

    @Override
    @Transactional(readOnly = true)
    public VetDto findByNPI(final String npi) {
        Assert.notNull(npi, "NPI must not be null!");

        LOG.debug("Retrieving Veterinarian with NPI '" + npi + "'");

        return veterinarianRepository.findVeterinarianByNPI(npi)
                .map(this::decorateVeterinarianDto)
                .orElseThrow(() -> new ResourceNotFoundException("No veterinarian found with npi '" + npi + "'"));
    }

    private VetDto decorateVeterinarianDto(Veterinarian veterinarian) {
        VetDto outgoingVetDto = vetMapper.mapToVetDto(veterinarian);
        ProviderIdDto providerIdDto = providerIdService.findProviderIdByPersonId(veterinarian.getId());
        outgoingVetDto.setProviderId(providerIdDto);
        return outgoingVetDto;
    }

    @Override
    @Transactional
    public PersonBasicDto updateVeterinarianBasicInfo(final UUID id, final PersonBasicDto resource) {
        Assert.notNull(id, "Id must not be null!");
        Assert.notNull(resource, "Vet Dto resource must not be null!");

        LOG.debug("Updating veterinarian basic info with id '" + id + "'");

        Optional<Veterinarian> veterinarian = veterinarianRepository.findVeterinarianById(id);
        if(!veterinarian.isPresent())
            throw new ResourceNotFoundException("No Veterinarian found!");

        String firstName = resource.getFirstName();
        String middleName = resource.getMiddleName();
        String lastName = resource.getLastName();

        Veterinarian vet = veterinarian.get();

        if(firstName != null && !firstName.equals(vet.getFirstName()))
            vet.setFirstName(firstName);
        if(middleName != null && !middleName.equals(vet.getMiddleName()))
            vet.setMiddleName(middleName);
        if (lastName != null && !lastName.equals(vet.getLastName()))
            vet.setLastName(lastName);

        return vetMapper.mapToPersonBasicDto(veterinarianRepository.saveAndFlush(vet));
    }

    //*****************************************************************************************************/
    //********************************************** Address **********************************************/
    //*****************************************************************************************************/

    @Override
    public AddressDto createAddress(final UUID id, final AddressDto resource) {
        return addressService.createAddress(id, resource);
    }

    @Override
    public AddressDto updateAddress(final UUID id, final UUID address_id, final AddressDto resource) {
        return addressService.updateAddress(id, address_id, resource);
    }

    @Override
    public void deleteAddress(final UUID id, final UUID address_id) {
        addressService.deleteAddress(id, address_id);
    }

    @Override
    public AddressDto findAddressById(final UUID id, final UUID address_id) {
        return addressService.findAddressById(id, address_id);
    }

    @Override
    public List<AddressDto> findAddressesByPersonId(final UUID id) {
        return addressService.findAddressesByPersonId(id);
    }

    //*****************************************************************************************************/
    //********************************************** Phone ************************************************/
    //*****************************************************************************************************/

    @Override
    public PhoneDto createPhone(final UUID id, final PhoneDto resource) {
        return phoneService.createPhone(id, resource);
    }

    @Override
    public PhoneDto updatePhone(final UUID id, final UUID phone_id, final PhoneDto resource) {
        return phoneService.updatePhone(id, phone_id, resource);
    }

    @Override
    public void deletePhone(final UUID id, final UUID phone_id) {
        phoneService.deletePhone(id, phone_id);
    }

    @Override
    public PhoneDto findPhoneById(final UUID id, final UUID phone_id) {
        return phoneService.findPhoneById(id, phone_id);
    }

    @Override
    public List<PhoneDto> findPhonesByPersonId(final UUID id) {
        return phoneService.findPhonesByPersonId(id);
    }

    //*****************************************************************************************************/
    //**************************************** Provider Identifier ****************************************/
    //*****************************************************************************************************/

    @Override
    public ProviderIdDto createProviderIdentifier(final UUID id, final ProviderIdDto resource) {
        return providerIdService.createProviderIdentifier(id, resource);
    }

    @Override
    public ProviderIdDto updateProviderIdentifier(final UUID id, final UUID provider_id, final ProviderIdDto resource) {
        return providerIdService.updateProviderIdentifier(id, provider_id, resource);
    }

    @Override
    public void deleteProviderIdentifier(final UUID id, final UUID provider_id) {
        providerIdService.deleteProviderIdentifier(id, provider_id);
    }

    @Override
    public ProviderIdDto findProviderIdById(final UUID id, final UUID provider_id) {
        return providerIdService.findProviderIdById(id, provider_id);
    }

    @Override
    public ProviderIdDto findProviderIdByPersonId(final UUID id) {
        return providerIdService.findProviderIdByPersonId(id);
    }

    //*****************************************************************************************************/
    //********************************************* Facility **********************************************/
    //*****************************************************************************************************/

    @Override
    public FacilityDto createFacility(final UUID id, final FacilityDto resource) {
        return facilityService.createFacility(id, resource);
    }

    @Override
    public FacilityDto updateFacility(final UUID id, final UUID facility_id, final FacilityDto resource) {
        return facilityService.updateFacility(id, facility_id, resource);
    }

    @Override
    public void deleteFacility(final UUID id, final UUID facility_id) {
        facilityService.deleteFacility(id, facility_id);
    }

    @Override
    public FacilityDto findFacilityById(final UUID id, final UUID facility_id) {
        return facilityService.findFacilityById(id, facility_id);
    }

    @Override
    public List<FacilityDto> findFacilitiesByPersonId(final UUID id) {
        return facilityService.findFacilitiesByPersonId(id);
    }

    //*****************************************************************************************************/
    //***************************************** Specialty *************************************************/
    //*****************************************************************************************************/

    @Override
    public SpecialtyDto createSpecialty(final UUID id, final SpecialtyDto resource) {
        return specialtyService.createSpecialty(id, resource);
    }

    @Override
    public SpecialtyDto updateSpecialty(final UUID id, final UUID specialty_id, final SpecialtyDto resource) {
        return specialtyService.updateSpecialty(id, specialty_id, resource);
    }

    @Override
    public void deleteSpecialty(final UUID id, final UUID specialty_id) {
        specialtyService.deleteSpecialty(id, specialty_id);
    }

    @Override
    public SpecialtyDto findSpecialtyById(final UUID id, final UUID specialty_id) {
        return specialtyService.findSpecialtyById(id, specialty_id);
    }

    @Override
    public List<SpecialtyDto> findSpecialtiesByPersonId(final UUID id) {
        return specialtyService.findSpecialtiesByPersonId(id);
    }

    //*****************************************************************************************************/
    //************************************* Professional Suffix *******************************************/
    //*****************************************************************************************************/

    @Override
    public ProfessionalSuffixDto createProfessionalSuffix(final UUID id, final ProfessionalSuffixDto resource) {
        return professionalSuffixService.createProfessionalSuffix(id, resource);
    }

    @Override
    public ProfessionalSuffixDto updateProfessionalSuffix(final UUID id, final UUID suffix_id, final ProfessionalSuffixDto updateDto) {
        return professionalSuffixService.updateProfessionalSuffix(id, suffix_id, updateDto);
    }

    @Override
    public void deleteProfessionalSuffix(UUID id, UUID suffix_id) {
        professionalSuffixService.deleteProfessionalSuffix(id, suffix_id);
    }

    @Override
    public ProfessionalSuffixDto findProfessionalSuffixById(final UUID id, final UUID suffix_id) {
        return professionalSuffixService.findProfessionalSuffixById(id, suffix_id);
    }

    @Override
    public List<ProfessionalSuffixDto> findProfessionalSuffixesByPersonId(final UUID id) {
        return professionalSuffixService.findProfessionalSuffixesByPersonId(id);
    }

    //*****************************************************************************************************/
    //**************************************** Signature *************************************************/
    //*****************************************************************************************************/

    @Override
    public SignatureDto createSignature(final UUID id, final SignatureDto resource) {
        return signatureService.createSignature(id, resource);
    }

    @Override
    public SignatureDto updateSignature(final UUID id, final UUID sig_id, final SignatureDto resource) {
        return signatureService.updateSignature(id, sig_id, resource);
    }

    @Override
    public void deleteSignature(final UUID id, final UUID sig_id) {
        signatureService.deleteSignature(id, sig_id);
    }

    @Override
    public SignatureDto findSignatureById(final UUID id, final UUID sig_id) {
        return signatureService.findSignatureById(id, sig_id);
    }

    @Override
    public SignatureDto findSignatureByPersonId(final UUID id) {
        return signatureService.findSignatureByPersonId(id);
    }

    //*****************************************************************************************************/
    //**************************************** Pet Owner  *************************************************/
    //*****************************************************************************************************/

    @Override
    public PetOwnerDto createPetOwner(final UUID id, final PetOwnerDto resource) {
        return petOwnerService.createPetOwner(id, resource);
    }

    @Override
    public PetOwnerDto findPetOwnerById(final UUID id, final UUID petOwnerId) {
        return petOwnerService.findPetOwnerById(id, petOwnerId);
    }
}
