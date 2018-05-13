package com.vdcrx.rest.domain.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

/**
 * Base entity
 *
 * @author Ranel del Pilar
 */

@MappedSuperclass
public abstract class BaseEntity {

    // Persistence id
    @Getter
    @Setter
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "UUID", unique = true, nullable = false, updatable = false, insertable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    // Internal ID for equals and hashcode
    @Getter
    @Transient
    private UUID internalId = UUID.randomUUID();

    @Override
    public String toString() {
        return "BaseEntity [internalId: " + internalId + "]; ";
    }

    public BaseEntity() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity)) return false;
        return getInternalId().equals(((BaseEntity) o).getInternalId());
    }

    @Override
    public int hashCode() {
        return getInternalId().hashCode();
    }
}
