package com.vdcrx.rest.domain.entities;

import com.vdcrx.rest.domain.enums.PhoneType;
import com.vdcrx.rest.domain.interfaces.IPerson;
import com.vdcrx.rest.validators.ValidPhone;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Phone entity
 *
 * @author Ranel del Pilar
 */

@Entity
@Audited
@Table(name = "PHONES")
public class Phone extends BaseEntity {

    public Phone() { super(); }

    public Phone(PhoneType phoneType, String phone, String phoneExt) {
        super();
        this.phoneType = phoneType;
        this.phone = phone;
        this.phoneExt = phoneExt;
    }

    @Getter
    @Setter
    @NotNull(message = "{message.Phone.phoneType.null}")
    @Enumerated(value = EnumType.STRING)
    @Column(name = "PHONE_TYPE", nullable = false, length = 32, columnDefinition = "VARCHAR(32) DEFAULT 'MOBILE'")
    private PhoneType phoneType = PhoneType.MOBILE;

    @Getter
    @Setter
    @NotBlank(message = "{message.Phone.phone.blank}")
    @ValidPhone(message = "{message.Phone.phone.validPhoneNumber}")
    @Column(name = "PHONE", nullable = false, length = 16, unique = false)
    private String phone;

    @Getter
    @Setter
    @Size(max = 6, message = "{message.Phone.phoneExt.size}")
    @Column(name = "PHONE_EXT")
    private String phoneExt;

    @Getter
    @Version
    @Column(name = "VERSION", nullable = false, columnDefinition = "INT(3) DEFAULT 0")
    protected long version = 0L;

    @NotNull(message = "{message.Phone.person.null}")
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
            prev.removePhone(this);

        if(person != null)
            ((Person) person).addPhone(this);
    }

    private boolean sameAsFormer(IPerson person) {
        return this.person == null ? person == null : this.person.equals(person);
    }

    @Override
    public String toString() {
        return "Phone [" +
                "phoneType: " + phoneType +
                ", phone: '" + phone +
                ", phoneExt: '" + phoneExt +
                ", version: " + version +
                "]; " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Phone)) return false;
        return getInternalId().equals(((Phone) o).getInternalId());
    }

    @Override
    public int hashCode() {
        return getInternalId().hashCode();
    }
}
