package com.vdcrx.rest.domain.entities;

import com.vdcrx.rest.domain.interfaces.IPerson;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Pet owner entity
 *
 * @author Ranel del Pilar
 */

@Entity
@DiscriminatorValue("PET_OWNER")
@NamedEntityGraph(name = "PetOwner.graph",
        attributeNodes = {
        @NamedAttributeNode("roles"),
        @NamedAttributeNode("addresses"),
        @NamedAttributeNode("phones"),
        @NamedAttributeNode("pets"),
        @NamedAttributeNode("veterinarians")})
public class PetOwner extends Person {

    ///////////////// Pet (Bi-directional Many-to-many) /////////////////

    @NotAudited
    @Valid
    @NotNull(message = "{message.PetOwner.pets.null}")
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "PET_OWNERS_PETS",
            joinColumns = @JoinColumn(name = "PET_OWNER_UUID"),
            inverseJoinColumns = @JoinColumn(name = "PETS_UUID"))
    private Set<Pet> pets;

    private Set<Pet> getPetsInternal() {
        if(this.pets == null)
            this.pets = new HashSet<>();

        return this.pets;
    }

    public Set<Pet> getPets() {
        Set<Pet> pets = new HashSet<>(getPetsInternal());
        return Collections.unmodifiableSet(pets);
    }

    public void addPet(Pet pet) {
        if (pet == null || getPetsInternal().contains(pet))
            return;

        getPetsInternal().add(pet);
        pet.addPetOwner(this);
    }

    public void removePet(Pet pet) {
        if(pet == null || !getPetsInternal().contains(pet))
            return;

        getPetsInternal().remove(pet);
        pet.removePetOwner(this);
    }

    ///////////////// Veterinarian (Bi-directional Many-to-many) ///////////////////

    @NotAudited
    @Valid
    @NotNull(message = "{message.PetOwner.veterinarians.null}")
    @ManyToMany(mappedBy = "petOwners", targetEntity = Veterinarian.class)
    private Set<Person> veterinarians;

    private Set<Person> getVeterinariansInternal() {
        if(this.veterinarians == null)
            this.veterinarians = new HashSet<>();

        return this.veterinarians;
    }

    public Set<IPerson> getVeterinarians() {
        Set<IPerson> veterinarians = new HashSet<>(getVeterinariansInternal());
        return Collections.unmodifiableSet(veterinarians);
    }

    public void addVeterinarian(IPerson person) {
        if(person == null || getVeterinariansInternal().contains(person))
            return;

        getVeterinariansInternal().add((Person) person);
        ((Veterinarian) person).addPetOwner(this);
    }

    public void removeVeterinarian(IPerson person) {
        if(person == null || !getVeterinariansInternal().contains(person))
            return;

        getVeterinariansInternal().remove(person);
        ((Veterinarian) person).removePetOwner(this);
    }

    @Override
    public String toString() {
        return "[Pet owner [" +
                "[Pet owners empty]; " +
                "[Veterinarians empty]; " +
                super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PetOwner)) return false;
        return getInternalId().equals(((PetOwner) o).getInternalId());
    }

    @Override
    public int hashCode() {
        return getInternalId().hashCode();
    }
}

