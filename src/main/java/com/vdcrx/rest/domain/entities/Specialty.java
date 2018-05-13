package com.vdcrx.rest.domain.entities;

import lombok.Getter;
import org.hibernate.envers.Audited;

import javax.persistence.*;

/**
 * Specialty entity
 *
 * @author Ranel del Pilar
 */

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE")
@Entity
@Audited
@Table(name = "SPECIALTIES")
public abstract class Specialty extends BaseEntity {

    public Specialty() { super(); }

    @Getter
    @Version
    @Column(name = "VERSION", nullable = false, columnDefinition = "INT(3) DEFAULT 0")
    private long version = 0L;

    @Override
    public String toString() {
        return "Facility [version: " + version + "]; " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Specialty)) return false;
        return getInternalId().equals(((Specialty) o).getInternalId());
    }

    @Override
    public int hashCode() { return getInternalId().hashCode(); }
}
