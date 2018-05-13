package com.vdcrx.rest.domain.entities;

import com.vdcrx.rest.domain.enums.AddressType;
import com.vdcrx.rest.domain.interfaces.IPerson;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Address entity
 * 
 * @author Ranel del Pilar
 */

@Entity
@Audited
@Table(name = "ADDRESSES")
public class Address extends BaseEntity {

    public Address() { super(); }

    public Address(String address1,
                   String address2,
                   String city,
                   String state,
                   String postalCode,
                   AddressType addressType) {
        super();
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.addressType = addressType;
    }

    @Getter
    @Setter
    @NotBlank(message = "{message.Address.address1.blank}")
    @Size(min = 3, max = 64, message = "{message.Address.address1.size}")
    @Column(name = "ADDRESS1", nullable = false, length = 64)
    private String address1;

    @Getter
    @Setter
    @Size(max = 64, message = "{message.Address.address2.size}")
    @Column(name = "ADDRESS2", length = 64)
    private String address2;

    @Getter
    @Setter
    @NotBlank(message = "{message.Address.city.blank}")
    @Size(min = 3, max = 50, message = "{message.Address.city.size}")
    @Column(name = "CITY", nullable = false, length = 50)
    private String city;

    @Getter
    @Setter
    @NotBlank(message = "{message.Address.state.blank}")
    @Size(min = 2, max = 50, message = "{message.Address.state.size}")
    @Column(name = "STATE", nullable = false, length = 50)
    private String state;

    @Getter
    @Setter
    @NotBlank(message = "{message.Address.postalCode.blank}")
    @Size(min = 5, max = 10, message = "{message.Address.postalCode.size}")
    @Column(name = "POSTAL_CODE", nullable = false, length = 10)
    private String postalCode;

    @Getter
    @Setter
    @NotNull(message = "{message.Address.AddressType.null}")
    @Enumerated(EnumType.STRING)
    @Column(name = "ADDRESS_TYPE", nullable = false, columnDefinition = "VARCHAR(20) NOT NULL", length = 20)
    private AddressType addressType;

    @Getter
    @Version
    @Column(name = "VERSION", nullable = false, columnDefinition = "INT(3) DEFAULT 0")
    private long version = 0L;

    @NotNull(message = "{message.Address.person.null}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSON_UUID", nullable = false)
    private Person person;

    public Person getPerson() {
        return person;
    }

    public void setPerson(IPerson person) {
        if(sameAsFormer(person))
            return;

        Person prev = this.person;
        this.person = (Person) person;

        if(prev != null)
            prev.removeAddress(this);

        if(person != null)
            ((Person) person).addAddress(this);
    }

    private boolean sameAsFormer(IPerson person) {
        return this.person == null ? person == null : this.person.equals(person);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Address [");
        sb.append("Address1: ").append(this.address1).append("; ");
        sb.append("Address2: ").append(this.address2).append("; ");
        sb.append("City: ").append(this.city).append("; ");
        sb.append("State: ").append(this.state).append("; ");
        sb.append("Postal code: ").append(this.postalCode).append(";");
        sb.append("Address type: ").append(this.addressType).append("; ");
        sb.append("Version: ").append(this.version).append("; ");
        sb.append("] ");

        sb.append("[Person empty]");
        sb.append(super.toString());
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        return getInternalId().equals(((Address) o).getInternalId());
    }

    @Override
    public int hashCode() {
        return getInternalId().hashCode();
    }
}
