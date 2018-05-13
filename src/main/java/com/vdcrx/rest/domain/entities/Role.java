package com.vdcrx.rest.domain.entities;

import com.vdcrx.rest.domain.enums.RoleType;
import com.vdcrx.rest.domain.interfaces.IPerson;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Role entity
 *
 * @author Ranel del Pilar
 */

@Entity
@Audited
@Table(name = "ROLES")
public class Role extends BaseEntity {

    public Role() {
        super();
    }

    public Role(RoleType name) {
        super();
        this.name = name;
    }

    @Getter
    @Setter
    @NotNull(message = "{message.Role.name.null}")
    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE_NAME", nullable = false, length = 32, columnDefinition = "VARCHAR(32) DEFAULT 'BASIC'")
    private RoleType name = RoleType.BASIC;

    @Getter
    @Version
    @Column(name = "VERSION", nullable = false, columnDefinition = "INT(3) DEFAULT 0")
    protected long version = 0L;

    @NotAudited
    @ManyToMany(mappedBy = "roles")
    private Set<Person> persons;

    public Set<Person> getPersonsInternal() {
        if(this.persons == null) {
            this.persons = new HashSet<>();
        }
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
        ((Person) person).addRole(this);
    }

    public void removePerson(IPerson person) {
        if(person == null || !getPersonsInternal().contains(person))
            return;

        getPersonsInternal().remove(person);
        ((Person) person).removeRole(this);
    }

    @Override
    public String toString() {
        return "Role [" +
                "name: " + name +
                ", version: " + version +
                "]; " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        return getInternalId().equals(((Role) o).getInternalId());

    }

    @Override
    public int hashCode() {
        return getInternalId().hashCode();
    }
}
