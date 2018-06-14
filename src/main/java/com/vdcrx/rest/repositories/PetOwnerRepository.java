package com.vdcrx.rest.repositories;

/*
    This is from the perspective of a user so services must be restricted.
    Another API will be created that will allow Administrative services.

    NOTE: SOME OF THESE WILL OVERLAP EACH OTHER
    Retrieve: PetOwner needs to know the following information...
    1. Retrieve one PetOwner by username
    2. Retrieve one PetOwner by email
    3. Retrieve one PetOwner by id

    (Limited pet information: name, photo.
    (All retrievals above must retrieve only required info (Lazy loading) for this PetOwner

    4. Retrieve one or more Addresses associated with this PetOwner
    5. Retrieve one veterinarian associated with this PetOwner (Limited Vet information)
    6. Retrieve zero or more orders associated with this PetOwner (Both active and sent, for historical purposes)

    Create
    1. Cannot create anything. Must only be able to update and view info associated with this PetOwner including orders

    Update
    1. Cannot update anything. It must be done through Vet or Pharmacy.

    Delete
    1. Cannot delete anything. It must be done through Vet or Pharmacy.
 */

//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface PetOwnerRepository {
    //    // 1.
//    PetOwner findPetOwnerByUserName(String username) throws DataAccessException;
//
//    // 2.
//    PetOwner findPetOwnerByEmail(String email) throws DataAccessException;
//
//    // 3.
//    PetOwner findPetOwnerByUuid(UUID id) throws DataAccessException;
//
//    // 4.
//    //@Query("SELECT po.veterinarianAddresses FROM PetOwner po WHERE po.id =:id")
//    //Collection<VeterinarianAddress> findPetOwnerAddressesByUUID(@Param("id") UUID id) throws DataAccessException;
//
//    // 5.
//
//    // 6.
//}
