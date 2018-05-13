package com.vdcrx.rest.domain.entities;

import com.vdcrx.rest.domain.enums.FacilityType;
import com.vdcrx.rest.domain.interfaces.IPerson;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Facility entity
 *
 * @author Ranel del Pilar
 */

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
@Audited
@Table(name = "FACILITIES")
public class Facility extends BaseEntity {

    public Facility() {
        super();
    }

    public Facility(FacilityType facilityType, String name) {
        super();
        this.facilityType = facilityType;
        this.name = name;
    }

    @Getter
    @Setter
    @NotNull(message = "{message.Facility.facilityType.null}")
    @Enumerated(value = EnumType.STRING)
    @Column(name = "FACILITY_TYPE", nullable = false, length = 32, columnDefinition = "VARCHAR(32) DEFAULT 'UNKNOWN'")
    private FacilityType facilityType = FacilityType.UNKNOWN;

    @Getter
    @Setter
    @NotBlank(message = "{message.Facility.name.blank}")
    @Size(max = 100, message ="{message.Facility.name.size}")
    @Column(name = "FACILITY_NAME", nullable = false, length = 100)
    private String name;

    @Getter
    @Version
    @Column(name = "VERSION", nullable = false, columnDefinition = "INT(3) DEFAULT 0")
    private long version = 0L;

    @NotAudited
    @NotNull(message = "{message.VeterinarianFacility.veterinarians.null}")
    @ManyToMany(mappedBy = "facilities", targetEntity = Veterinarian.class)
    private Set<Person> persons;

    private Set<Person> getPersonsInternal() {
        if (this.persons == null)
            this.persons = new HashSet<>();
        return this.persons;
    }

    public Set<IPerson> getPersons() {
        Set<IPerson> persons = new HashSet<>(getPersonsInternal());
        return Collections.unmodifiableSet(persons);
    }

    public void addPerson(IPerson person) {
        if(person == null || getPersonsInternal().contains(person))
            return;

        getPersonsInternal().add((Person) person);
        ((Veterinarian) person).addFacility(this);
    }

    public void removePerson(IPerson person) {
        if(person == null || !getPersonsInternal().contains(person))
            return;

        getPersonsInternal().remove(person);
        ((Veterinarian) person).removeFacility(this);
    }

    @Override
    public String toString() {
        return "Facility [" +
                "facilityType: " + facilityType +
                ", name: '" + name +
                ", version: " + version +
                "]; [Persons empty]; " +
                super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Facility)) return false;
        return getInternalId().equals(((Facility) o).getInternalId());
    }

    @Override
    public int hashCode() {
        return getInternalId().hashCode();
    }
}
