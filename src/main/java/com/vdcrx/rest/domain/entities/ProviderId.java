package com.vdcrx.rest.domain.entities;

import com.vdcrx.rest.domain.enums.ProviderIdType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Provider identifier entity
 *
 * @author Ranel del Pilar
 */

@Entity
@Audited
// TODO: @ValidProviderId
@Table(name = "PROVIDER_IDS")
public class ProviderId extends BaseEntity {

    public ProviderId() {
        super();
    }

    public ProviderId(ProviderIdType providerIdType, String dea, String me, String npi) {
        super();
        this.providerIdType = providerIdType;
        this.dea = dea;
        this.me = me;
        this.npi = npi;
    }

    @Getter
    @Setter
    @NotNull(message = "{message.ProviderId.ProviderIdType.null}")
    @Column(name = "PROVIDER_ID_TYPE", nullable = false, length = 32, columnDefinition = "VARCHAR(40) DEFAULT 'NONE'")
    @Enumerated(EnumType.STRING)
    private ProviderIdType providerIdType = ProviderIdType.NONE;

    @Getter
    @Setter
    @NotBlank(message = "{message.ProviderId.dea.blank}")
    @Size(min = 10, max = 10, message = "{message.ProviderId.dea.size}")
    @Column(name = "DEA", unique = true, nullable = false, length = 10)
    private String dea;

    @Getter
    @Setter
    @NotBlank(message = "{message.ProviderId.me.blank}")
    @Size(min = 11, max = 11, message = "{message.ProviderId.me.size}")
    @Column(name = "ME", unique = true, nullable = false, length = 11)
    private String me;

    @Getter
    @Setter
    @NotBlank(message = "{message.ProviderId.npi.blank}")
    @Size(min = 10, max = 10, message = "{message.ProviderId.npi.size}")
    @Column(name = "NPI", unique = true, nullable = false, length = 10)
    private String npi;

    @Getter
    @Version
    @Column(name = "VERSION", nullable = false, columnDefinition = "INT(3) DEFAULT 0")
    private long version = 0L;

    // Provider Identifier Entity (Uni-directional One-to-one with Veterinarian Entity) //

    @Getter
    @Setter
    @NotAudited
    @NotNull(message = "{message.ProviderId.person.null}")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSON_ID", unique = true, nullable = false, updatable = false)
    private Person person;

    @Override
    public String toString() {
        return "ProviderId [" +
                "dea: '" + dea +
                ", me: '" + me +
                ", npi: '" + npi +
                ", version: " + version + "]; " +
                "[Person is empty]; " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProviderId)) return false;
        return getInternalId().equals(((ProviderId) o).getInternalId());
    }

    @Override
    public int hashCode() {
        return getInternalId().hashCode();
    }
}
