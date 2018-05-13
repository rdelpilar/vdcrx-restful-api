package com.vdcrx.rest.domain.entities;

import com.vdcrx.rest.domain.enums.PetAllergyType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Pet allergy entity
 *
 * @author Ranel del Pilar
 */

@Entity
@DiscriminatorValue("PET")
public class PetAllergy extends Allergy {

    public PetAllergy() { super(); }

    public PetAllergy(PetAllergyType petAllergyType) {
        super();
        this.petAllergyType = petAllergyType;
    }

    @Getter
    @Setter
    @NotNull(message = "{message.Allergy.petAllergyType.null}")
    @Enumerated(value = EnumType.STRING)
    @Column(name = "PET_ALLERGY_TYPE", nullable = false, length = 32, columnDefinition = "VARCHAR(32) DEFAULT 'NONE'")
    private PetAllergyType petAllergyType = PetAllergyType.NONE;

    @NotAudited
    @NotNull(message = "{message.PetAllergy.pets.null}")
    @ManyToMany(mappedBy = "allergies")
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
        if (pet == null || getPetsInternal().contains(pet)) {
            return;
        }

        getPetsInternal().add(pet);
        pet.addPetAllergy(this);
    }

    public void removePet(Pet pet) {
        if(pet == null || !getPetsInternal().contains(pet))
            return;

        getPetsInternal().remove(pet);
        pet.removePetAllergy(this);
    }

    @Override
    public String toString() {
        return "Pet Allergy [" +
                "Pet allergy type: " + petAllergyType +
                "]; " +
                "[Pets empty]; " +
                super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PetAllergy)) return false;
        return getInternalId().equals(((PetAllergy) o).getInternalId());
    }

    @Override
    public int hashCode() {
        return getInternalId().hashCode();
    }
}
