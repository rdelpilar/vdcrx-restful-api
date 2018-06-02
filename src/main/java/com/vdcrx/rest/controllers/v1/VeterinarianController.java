package com.vdcrx.rest.controllers.v1;

import com.vdcrx.rest.api.v1.hateoas.events.PaginatedResultsRetrievedEvent;
import com.vdcrx.rest.api.v1.model.dto.*;
import com.vdcrx.rest.exceptions.ResourceNotFoundException;
import com.vdcrx.rest.services.VeterinarianService;
import com.vdcrx.rest.validators.ValidEmail;
import com.vdcrx.rest.validators.ValidPhone;
import com.vdcrx.rest.validators.ValidUsername;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static com.vdcrx.rest.constants.ApiConstants.VET_BASE_PATH;
import static com.vdcrx.rest.constants.ApiConstants.VET_SIGNUP_PATH;

/*

    -- Retrieve pets in PetController
    4. Retrieve one pet by name that is a patient of this vet
    5. Retrieve one pet by id that is a patient of this vet
    6. Retrieve one or more pets by id of PetOwner

    -- ??
    7. Retrieve one or more pets by first name of PetOwner
    8. Retrieve one or more pets by last name of PetOwner

    -- ??
    9. Retrieve all pets that are patients of this vet
    10. Retrieve all pets that are patients of this vet and are owned by PetOwner


    // DO YOU NEED TO SEND A VETERINARIAN JSON OBJECT BACK TO CLIENT AFTER CREATE?
    Create
    1. Create one veterinarian but must be activated by an application administrator on the Admin version of the API.
    2. Create one or more PetOwners that will be tied to this vet given valid data.
    3. Create one or more pets that will be tied to this vet given valid data.

    Update
    1. Update this veterinarian's own profile.
    2. Update a PetOwner information using the PetOwner's id. Must be owned by this vet.
    3. Update a pet's information using the pet's id. Must be owned by this vet.

    Delete
    1. Delete a VeterinarianEntity field or fields only if they are nullable.
    2. Cannot delete information from Professional Identifier.
    3. Delete a PetOwner field owned by this vet.
    4. Delete a PetOwner owned by this vet.
    5. Delete a SpeciesType field owned by this vet.
 */


/**
 * Veterinarian controller
 *
 * @author Ranel del Pilar
 */

@Validated
@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
public class VeterinarianController {

    private ApplicationEventPublisher eventPublisher;
    private VeterinarianService service;

    @Autowired
    public VeterinarianController(ApplicationEventPublisher eventPublisher, VeterinarianService service) {
        this.eventPublisher = eventPublisher;
        this.service = service;
    }

    //*****************************************************************************************************/
    //********************************************** Veterinarian *****************************************/
    //*****************************************************************************************************/

    @PostMapping(value = VET_SIGNUP_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    public VetDto createVeterinarian(@Valid @Validated @RequestBody final VetDto dto) {
        return service.createVeterinarian(dto);
    }

    @PatchMapping(value = {VET_BASE_PATH + "/{id}"})
    @ResponseStatus(HttpStatus.OK)
    public PersonBasicDto updateVeterinarianBasicInfo(@PathVariable @Valid final UUID id,
                                          @Valid @Validated @RequestBody final PersonBasicDto resource) {
        return service.updateVeterinarianBasicInfo(id, resource);
    }

    // /api/v1/veterinarian/id/{id} - GET
    @GetMapping({VET_BASE_PATH + "/id/{id}"})
    @ResponseStatus(HttpStatus.OK)
    public VetDto findVeterinarianById(@PathVariable @Valid final UUID id) {
        return service.findOne(id);
    }

    // /api/v1/veterinarian/id2/{id} - GET
    @GetMapping({VET_BASE_PATH + "/id2/{id}"})
    @ResponseStatus(HttpStatus.OK)
    public VetDto findVeterinarianById2(@PathVariable @Valid final UUID id) {
        return service.findById(id);
    }

    // /api/v1/veterinarian/username/{username} - GET
    @GetMapping({VET_BASE_PATH + "/username/{username}"})
    @ResponseStatus(HttpStatus.OK)
    public VetDto findVeterinarianByUsername(@PathVariable @ValidUsername final String username) {
        return service.findByUsername(username);
    }

    // /api/v1/veterinarian/email/{email} - GET
    @GetMapping({VET_BASE_PATH + "/email/{email}"})
    @ResponseStatus(HttpStatus.OK)
    public VetDto findVeterinarianByEmail(@PathVariable @ValidEmail final String email) {
        return service.findByEmail(email);
    }

    // /api/v1/veterinarian/phone/{phone} - GET
    @GetMapping({VET_BASE_PATH + "/phone/{phone}"})
    @ResponseStatus(HttpStatus.OK)
    public List<VetDto> findVeterinariansByPhone(@PathVariable @ValidPhone final String phone) {
        return service.findByPhone(phone);
    }

    // /api/v1/veterinarian/dea/{dea} - GET
    @GetMapping({VET_BASE_PATH + "/dea/{dea}"})
    @ResponseStatus(HttpStatus.OK)
    public VetDto findVeterinarianByDEA(@PathVariable final String dea) {
        return service.findByDEA(dea);
    }

    // /api/v1/veterinarian/me/{me} - GET
    @GetMapping({VET_BASE_PATH + "/me/{me}"})
    @ResponseStatus(HttpStatus.OK)
    public VetDto findVeterinarianByME(@PathVariable final String me) {
        return service.findByME(me);
    }

    // /api/v1/veterinarian/npi/{npi} - GET
    @GetMapping({VET_BASE_PATH + "/npi/{npi}"})
    @ResponseStatus(HttpStatus.OK)
    public VetDto findVeterinarianByNPI(@PathVariable final String npi) {
        return service.findByNPI(npi);
    }

    // TEMPORARY
    @GetMapping({VET_BASE_PATH + "/list"})
    @ResponseStatus(HttpStatus.OK)
    public VetCollDto getAll() {
        return new VetCollDto(service.getAll().size(), service.getAll());
    }

    // Temporary
    @GetMapping(value = {VET_BASE_PATH + "/list"}, params = {"page", "size"})
    @ResponseStatus(HttpStatus.OK)
    public Page<VetDto> getListOfVeterinarians(@RequestParam("page") final int page,
                                               @RequestParam("size") final int size,
                                               UriComponentsBuilder uriBuilder,
                                               HttpServletRequest request,
                                               HttpServletResponse response) {

        Page<VetDto> resultPage = service.findPaginated(page, size);
        if(page > resultPage.getTotalPages())
            throw new ResourceNotFoundException("No veterinarian found!");

        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                VetDto.class, uriBuilder, request, response, page, resultPage.getTotalPages(), size));

        return resultPage;
    }

    // /api/v1/veterinarian/last_name/del pi?page=0&size=10
    @GetMapping(value = {VET_BASE_PATH + "/last_name/{lastName}"}, params = {"page", "size"})
    @ResponseStatus(HttpStatus.OK)
    public List<VetDto> getVeterinariansByLastNameIsStartingWith(@PathVariable final String lastName,
                                                                 @RequestParam("page") final int page,
                                                                 @RequestParam("size") final int size,
                                                                 UriComponentsBuilder uriBuilder,
                                                                 HttpServletRequest request,
                                                                 HttpServletResponse response) {

        List<VetDto> dtos = service.findByLastNameIsStartingWith(lastName, page, size);
        if(page > dtos.size())
            throw new ResourceNotFoundException("No Veterinarian found!");

        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                VetDto.class, uriBuilder, request, response, page, dtos.size(), size));

        return dtos;
    }

    // /api/v1/veterinarian/first_name/ranel?page=0&size=5
    @GetMapping(value = {VET_BASE_PATH + "/first_name/{firstName}"}, params = {"page", "size"})
    @ResponseStatus(HttpStatus.OK)
    public List<VetDto> getVeterinariansByFirstNameIsStartingWith(@PathVariable final String firstName,
                                                                  @RequestParam("page") final int page,
                                                                  @RequestParam("size") final int size,
                                                                  UriComponentsBuilder uriBuilder,
                                                                  HttpServletRequest request,
                                                                  HttpServletResponse response) {

        List<VetDto> dtos = service.findByFirstNameIsStartingWith(firstName, page, size);
        if(page > dtos.size())
            throw new ResourceNotFoundException("No Veterinarian found!");

        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                VetDto.class, uriBuilder, request, response, page, dtos.size(), size));

        return dtos;
    }

    //*****************************************************************************************************/
    //********************************************** Address **********************************************/
    //*****************************************************************************************************/

    // /api/v1/veterinarian/{id}/address
    @PostMapping(value = {VET_BASE_PATH + "/{id}/address"})
    @ResponseStatus(HttpStatus.CREATED)
    public AddressDto createAddress(@PathVariable @Valid final UUID id,
                                    @Valid @Validated @RequestBody final AddressDto resource) {
        return service.createAddress(id, resource);
    }

    @PatchMapping(value = {VET_BASE_PATH + "/{id}/address/{address_id}"})
    @ResponseStatus(HttpStatus.OK)
    public AddressDto updateAddress(@PathVariable @Valid final UUID id,
                                    @PathVariable @Valid final UUID address_id,
                                    @Valid @Validated @RequestBody final AddressDto resource) {
        return service.updateAddress(id, address_id, resource);
    }

    @DeleteMapping(value = {VET_BASE_PATH + "/{id}/address/{address_id}"})
    @ResponseStatus(HttpStatus.OK)
    public void deleteAddress(@PathVariable @Valid final UUID id,
                              @PathVariable @Valid final UUID address_id) {
        service.deleteAddress(id, address_id);
    }

    @GetMapping(value = {VET_BASE_PATH + "/{id}/address/{address_id}"})
    @ResponseStatus(HttpStatus.OK)
    public AddressDto findAddressById(@PathVariable @Valid final UUID id,
                                      @PathVariable @Valid final UUID address_id) {
        return service.findAddressById(id, address_id);
    }

    @GetMapping(value = {VET_BASE_PATH + "/{id}/addresses"})
    @ResponseStatus(HttpStatus.OK)
    public List<AddressDto> findAddressesByPersonId(@PathVariable @Valid final UUID id) {
        return service.findAddressesByPersonId(id);
    }

    //*****************************************************************************************************/
    //********************************************** Phone ************************************************/
    //*****************************************************************************************************/

    // /api/v1/veterinarian/{id}/phone
    @PostMapping(value = {VET_BASE_PATH + "/{id}/phone"})
    @ResponseStatus(HttpStatus.CREATED)
    public PhoneDto createPhone(@PathVariable @Valid final UUID id,
                                @Valid @Validated @RequestBody final PhoneDto resource) {
        return service.createPhone(id, resource);
    }

    @PatchMapping(value = {VET_BASE_PATH + "/{id}/phone/{phone_id}"})
    @ResponseStatus(HttpStatus.OK)
    public PhoneDto updatePhone(@PathVariable @Valid final UUID id,
                                @PathVariable @Valid final UUID phone_id,
                                @Valid @Validated @RequestBody final PhoneDto resource) {
        return service.updatePhone(id, phone_id, resource);
    }

    @DeleteMapping(value = {VET_BASE_PATH + "/{id}/phone/{phone_id}"})
    @ResponseStatus(HttpStatus.OK)
    public void deletePhone(@PathVariable @Valid final UUID id,
                            @PathVariable @Valid final UUID phone_id) {
        service.deletePhone(id, phone_id);
    }

    @GetMapping(value = {VET_BASE_PATH + "/{id}/phone/{phone_id}"})
    @ResponseStatus(HttpStatus.OK)
    public PhoneDto findPhoneById(@PathVariable @Valid final UUID id,
                                  @PathVariable @Valid final UUID phone_id) {
        return service.findPhoneById(id, phone_id);
    }

    @GetMapping(value = {VET_BASE_PATH + "/{id}/phones"})
    @ResponseStatus(HttpStatus.OK)
    public List<PhoneDto> findPhonesByPersonId(@PathVariable @Valid final UUID id) {
        return service.findPhonesByPersonId(id);
    }

    //*****************************************************************************************************/
    //**************************************** Provider Identifier ****************************************/
    //*****************************************************************************************************/

    // /api/v1/veterinarian/{id}/provider_identifier
    @PostMapping(value = {VET_BASE_PATH + "/{id}/provider_identifier"})
    @ResponseStatus(HttpStatus.CREATED)
    public ProviderIdDto createProviderIdentifier(@PathVariable @Valid final UUID id,
                                                  @Valid @Validated @RequestBody final ProviderIdDto resource) {
        return service.createProviderIdentifier(id, resource);
    }

    @PatchMapping(value = {VET_BASE_PATH + "/{id}/provider_identifier/{provider_id}"})
    @ResponseStatus(HttpStatus.OK)
    public ProviderIdDto updateProviderIdentifier(@PathVariable @Valid final UUID id,
                                                  @PathVariable @Valid final UUID provider_id,
                                                  @Valid @Validated @RequestBody final ProviderIdDto resource) {
        return service.updateProviderIdentifier(id, provider_id, resource);
    }

    @DeleteMapping(value = {VET_BASE_PATH + "/{id}/provider_identifier/{provider_id}"})
    @ResponseStatus(HttpStatus.OK)
    public void deleteProvider(@PathVariable @Valid final UUID id,
                               @PathVariable @Valid final UUID provider_id) {
        service.deleteProviderIdentifier(id, provider_id);
    }

    @GetMapping(value = {VET_BASE_PATH + "/{id}/provider_identifier/{provider_id}"})
    @ResponseStatus(HttpStatus.OK)
    public ProviderIdDto findProviderIdentifierById(@PathVariable @Valid final UUID id,
                                                    @PathVariable @Valid final UUID provider_id) {
        return service.findProviderIdById(id, provider_id);
    }

    @GetMapping(value = {VET_BASE_PATH + "/{id}/provider_identifier"})
    @ResponseStatus(HttpStatus.OK)
    public ProviderIdDto findProviderIdentifierByPersonId(@PathVariable @Valid final UUID id) {
        return service.findProviderIdByPersonId(id);
    }

    //*****************************************************************************************************/
    //********************************************* Facility **********************************************/
    //*****************************************************************************************************/

    // /api/v1/veterinarian/{id}/facility
    @PostMapping(value = {VET_BASE_PATH + "/{id}/facility"})
    @ResponseStatus(HttpStatus.CREATED)
    public FacilityDto createFacility(@PathVariable @Valid final UUID id,
                                      @Valid @Validated @RequestBody final FacilityDto resource) {
        return service.createFacility(id, resource);
    }

    @PatchMapping(value = {VET_BASE_PATH + "/{id}/facility/{facility_id}"})
    @ResponseStatus(HttpStatus.OK)
    public FacilityDto updateFacility(@PathVariable @Valid final UUID id,
                                      @PathVariable @Valid final UUID facility_id,
                                      @Valid @Validated @RequestBody final FacilityDto resource) {
        return service.updateFacility(id, facility_id, resource);
    }

    @DeleteMapping(value = {VET_BASE_PATH + "/{id}/facility/{facility_id}"})
    @ResponseStatus(HttpStatus.OK)
    public void deleteFacility(@PathVariable @Valid final UUID id,
                               @PathVariable @Valid final UUID facility_id) {
        service.deleteFacility(id, facility_id);
    }

    @GetMapping(value = {VET_BASE_PATH + "/{id}/facility/{facility_id}"})
    @ResponseStatus(HttpStatus.OK)
    public FacilityDto findFacilityById(@PathVariable @Valid final UUID id,
                                        @PathVariable @Valid final UUID facility_id) {
        return service.findFacilityById(id, facility_id);
    }

    @GetMapping(value = {VET_BASE_PATH + "/{id}/facilities"})
    @ResponseStatus(HttpStatus.OK)
    public List<FacilityDto> findFacilitiesByPersonId(@PathVariable @Valid final UUID id) {
        return service.findFacilitiesByPersonId(id);
    }


    //*****************************************************************************************************/
    //********************************************* Specialty *********************************************/
    //*****************************************************************************************************/

    // /api/v1/veterinarian/{id}/specialty
    @PostMapping(value = {VET_BASE_PATH + "/{id}/specialty"})
    @ResponseStatus(HttpStatus.CREATED)
    public SpecialtyDto createSpecialty(@PathVariable @Valid final UUID id,
                                        @Valid @Validated @RequestBody final SpecialtyDto resource) {
        return service.createSpecialty(id, resource);
    }

    @PatchMapping(value = {VET_BASE_PATH + "/{id}/specialty/{specialty_id}"})
    @ResponseStatus(HttpStatus.OK)
    public SpecialtyDto updateSpecialty(@PathVariable @Valid final UUID id,
                                        @PathVariable @Valid final UUID specialty_id,
                                        @Valid @Validated @RequestBody final SpecialtyDto resource) {
        return service.updateSpecialty(id, specialty_id, resource);
    }

    @DeleteMapping(value = {VET_BASE_PATH + "/{id}/specialty/{specialty_id}"})
    @ResponseStatus(HttpStatus.OK)
    public void deleteSpecialty(@PathVariable @Valid final UUID id,
                                @PathVariable @Valid final UUID specialty_id) {
        service.deleteSpecialty(id, specialty_id);
    }

    @GetMapping(value = {VET_BASE_PATH + "/{id}/specialty/{specialty_id}"})
    @ResponseStatus(HttpStatus.OK)
    public SpecialtyDto findSpecialtyById(@PathVariable @Valid final UUID id,
                                          @PathVariable @Valid final UUID specialty_id) {
        return service.findSpecialtyById(id, specialty_id);
    }

    @GetMapping(value = {VET_BASE_PATH + "/{id}/specialties"})
    @ResponseStatus(HttpStatus.OK)
    public List<SpecialtyDto> findSpecialtiesByPersonId(@PathVariable @Valid final UUID id) {
        return service.findSpecialtiesByPersonId(id);
    }

    //*****************************************************************************************************/
    //************************************* Professional Suffix  ******************************************/
    //*****************************************************************************************************/

    // /api/v1/veterinarian/{id}/professional_suffix
    @PostMapping(value = {VET_BASE_PATH + "/{id}/professional_suffix"})
    @ResponseStatus(HttpStatus.CREATED)
    public ProfessionalSuffixDto createProfessionalSuffix(@PathVariable @Valid final UUID id,
                                                          @Valid @Validated @RequestBody final ProfessionalSuffixDto resource) {
        return  service.createProfessionalSuffix(id, resource);
    }

    @PatchMapping(value = {VET_BASE_PATH + "/{id}/professional_suffix/{suffix_id}"})
    @ResponseStatus(HttpStatus.OK)
    public ProfessionalSuffixDto updateProfessionalSuffix(@PathVariable @Valid final UUID id,
                                                          @PathVariable @Valid final UUID suffix_id,
                                                          @Valid @Validated @RequestBody final ProfessionalSuffixDto resource) {
        return service.updateProfessionalSuffix(id, suffix_id, resource);
    }

    @DeleteMapping(value = {VET_BASE_PATH + "/{id}/professional_suffix/{suffix_id}"})
    @ResponseStatus(HttpStatus.OK)
    public void deleteProfessionalSuffix(@PathVariable @Valid final UUID id,
                                         @PathVariable @Valid final UUID suffix_id) {
        service.deleteProfessionalSuffix(id, suffix_id);
    }

    @GetMapping(value = {VET_BASE_PATH + "/{id}/professional_suffix/{suffix_id}"})
    @ResponseStatus(HttpStatus.OK)
    public ProfessionalSuffixDto findProfessionalSuffixById(@PathVariable @Valid final UUID id,
                                                            @PathVariable @Valid final UUID suffix_id) {
        return service.findProfessionalSuffixById(id, suffix_id);
    }

    @GetMapping(value = {VET_BASE_PATH + "/{id}/professional_suffix"})
    @ResponseStatus(HttpStatus.OK)
    public List<ProfessionalSuffixDto> findProfessionalSuffixesByPersonId(@PathVariable @Valid final UUID id) {
        return service.findProfessionalSuffixesByPersonId(id);
    }

    //*****************************************************************************************************/
    //***************************************** Signature  ************************************************/
    //*****************************************************************************************************/

    // /api/v1/veterinarian/{id}/signature
    @PostMapping(value = {VET_BASE_PATH + "/{id}/signature"})
    @ResponseStatus(HttpStatus.CREATED)
    public SignatureDto createSignature(@PathVariable @Valid final UUID id,
                                        @Valid @Validated @RequestBody final SignatureDto resource) {
        return service.createSignature(id, resource);
    }

    @PatchMapping(value = {VET_BASE_PATH + "/{id}/signature/{sig_id}"})
    @ResponseStatus(HttpStatus.OK)
    public SignatureDto updateSignature(@PathVariable @Valid final UUID id,
                                        @PathVariable @Valid final UUID sig_id,
                                        @Valid @Validated @RequestBody final SignatureDto resource) {
        return service.updateSignature(id, sig_id, resource);
    }

    @DeleteMapping(value = {VET_BASE_PATH + "/{id}/signature/{sig_id}"})
    @ResponseStatus(HttpStatus.OK)
    public void deleteSignature(@PathVariable @Valid final UUID id,
                                @PathVariable @Valid final UUID sig_id) {
        service.deleteSignature(id, sig_id);
    }

    @GetMapping(value = {VET_BASE_PATH + "/{id}/signature/{sig_id}"})
    @ResponseStatus(HttpStatus.OK)
    public SignatureDto findSignatureById(@PathVariable @Valid final UUID id,
                                          @PathVariable @Valid final UUID sig_id) {
        return service.findSignatureById(id, sig_id);
    }

    @GetMapping(value = {VET_BASE_PATH + "/{id}/signature"})
    @ResponseStatus(HttpStatus.OK)
    public SignatureDto findSignatureByPersonId(@PathVariable @Valid final UUID id) {
        return service.findSignatureByPersonId(id);
    }

    //*****************************************************************************************************/
    //***************************************** Pet Owner  ************************************************/
    //*****************************************************************************************************/

//    11. Retrieve one PetOwner that is a customer of this vet
//    12. Retrieve all PetOwner that is a customer of this vet
//    13. Retrieve one PetOwner that is a customer of this vet and by name of their pet

//    /api/v1/veterinarian/{id}/pet_owner_TODO                         Add pet owner (POST)
//    /api/v1/veterinarian/{id}/pet_owner_TODO/list                    List pet owners (GET)
//    /api/v1/veterinarian/{id}/pet_owner_TODO/{id}                  View/edit pet owner (GET, PUT, PATCH)
//    /api/v1/veterinarian/{id}/pet_owner_TODO/{id}/address          Add pet owner address (POST)
//
//    /api/v1/veterinarian/{id}/pet_owner_TODO/{id}/pet              Add pet (POST)
//    /api/v1/veterinarian/{id}/pet_owner_TODO/{id}/pet/list         List pets (GET)
//    /api/v1/veterinarian/{id}/pet_owner_TODO/{id}/pet/{id}       View/edit/delete pet (GET, PUT, PATCH, DELETE)
//
//    /api/v1/veterinarian/{id}/pet_owner_TODO/{id}/order            Add order for pet owner (POST)
//    /api/v1/veterinarian/{id}/pet_owner_TODO/{id}/order/list       List orders for pet owner (GET)
//    /api/v1/veterinarian/{id}/pet_owner_TODO/{id}/order/{id}     View only order and status for pet owner (GET)
//
//    /api/v1/veterinarian/{id}/pet_owner_TODO/{id}/card             Add Credit Card
//    /api/v1/veterinarian/{id}/pet_owner_TODO/{id}/card/list        List credit cards for pet owner (GET)
//    /api/v1/veterinarian/{id}/pet_owner_TODO/{id}/card/{id}      View/edit credit card (GET, PUT, PATCH)

    @GetMapping(value = {VET_BASE_PATH + "/{vet_id}/pet_owner/{pet_owner_id}"})
    @ResponseStatus(HttpStatus.OK)
    public PetOwnerDto findPetOwnerById(@PathVariable @Valid final UUID vet_id,
                                        @PathVariable @Valid final UUID pet_owner_id) {
        return service.findPetOwnerById(vet_id, pet_owner_id);
    }

}
