package com.vdcrx.rest.domain.entities;

import com.vdcrx.rest.domain.interfaces.IPerson;
import com.vdcrx.rest.validators.ValidEmail;
import com.vdcrx.rest.validators.ValidName;
import com.vdcrx.rest.validators.ValidUsername;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Person entity
 *
 * @author Ranel del Pilar
 */

@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "PERSON_TYPE")
@Entity
@Audited
@Table(name = "PERSONS")
@NamedEntityGraph(name = "Person.roles.graph",
        attributeNodes = {@NamedAttributeNode("roles")})
public abstract class Person extends BaseEntity implements IPerson {

    public Person() { super(); }

    @Getter
    @Setter
    @NotBlank(message = "{message.Person.username.blank}")
    @ValidUsername(message = "{message.Person.username.validUsername}")
    @Column(name = "USER_NAME", nullable = false, length = 32, unique = true)
    private String username;

    @Getter
    @Setter
    @NotBlank(message = "{message.Person.hashedPassword.blank}")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[A-Z]).{60}$", message = "{message.Person.hashedPassword.pattern}")
    @Column(name = "HASHED_PASSWORD", nullable = false, length = 60)
    private String hashedPassword;

    @Getter
    @Setter
    @NotBlank(message = "{message.Person.firstName.blank}")
    @ValidName(message = "{message.Person.firstName.validName}")
    @Column(name = "FIRST_NAME", nullable = false, length = 32)
    private String firstName;

    @Getter
    @Setter
    @Size(max = 32, message = "{message.Person.middleName.size}")
    @Column(name = "MIDDLE_NAME", length = 32)
    private String middleName;

    @Getter
    @Setter
    @NotBlank(message = "{message.Person.lastName.blank}")
    @ValidName(message = "{message.Person.lastName.validName}")
    @Column(name = "LAST_NAME", nullable = false, length = 32)
    private String lastName;

    @Getter
    @Setter
    @NotBlank(message = "{message.Person.email.blank}")
    @ValidEmail(message = "{message.Person.email.validEmail}")
    @Column(name = "EMAIL", nullable = false, length = 64, unique = true)
    private String email;

    @Getter
    @Setter
    @NotNull(message = "{message.Person.emailConfirmed.null}")
    @Column(name = "EMAIL_CONFIRMED", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean emailConfirmed;

    @Getter
    @Setter
    @NotNull(message = "{message.Person.loginEnabled.null}")
    @Column(name = "LOGIN_ENABLED", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean loginEnabled;

    @Getter
    @Setter
    @NotNull(message = "{message.Person.accountNonExpired.null}")
    @Column(name = "ACCOUNT_NON_EXPIRED", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean accountNonExpired = true;

    @Getter
    @Setter
    @NotNull(message = "{message.Person.accountNonLocked.null}")
    @Column(name = "ACCOUNT_NON_LOCKED", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean accountNonLocked = true;

    @Getter
    @Setter
    @NotNull(message = "{message.Person.credentialsNonExpired.null}")
    @Column(name = "CREDENTIALS_NON_EXPIRED", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean credentialsNonExpired = true;

    @Getter
    @Setter
    @NotNull(message = "{message.Person.twoFactorEnabled.null}")
    @Column(name = "TWO_FACTOR_ENABLED", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean twoFactorEnabled = true;

    @Getter
    @Setter
    @NotNull(message = "{message.Person.accessFailedCount.null}")
    @Max(value = 6, message = "{message.Person.accessFailedCount.max}")
    @Column(name = "ACCESS_FAILED_COUNT", nullable = false, columnDefinition = "SMALLINT(100) DEFAULT 0")
    private int accessFailedCount;

    @Getter
    @Setter
    @NotNull(message = "{message.Person.lastPasswordResetDate.null}")
    @Column(name = "LAST_PASSWORD_RESET_DATE", nullable = false, columnDefinition = "BIGINT(20) NOT NULL DEFAULT 0")
    private long lastPasswordResetDate;

    @Getter
    @Version
    @Column(name = "VERSION", nullable = false, columnDefinition = "INT(3) DEFAULT 0")
    private long version = 0L;

    ///////////////// Role (Bi-directional Many-to-many) /////////////////////

    @NotAudited
    @Valid
    @NotNull(message = "{message.Person.roles.null}")
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "PERSONS_ROLES",
            joinColumns = @JoinColumn(
                    name = "PERSON_UUID",
                    referencedColumnName = "UUID",
                    columnDefinition = "BINARY(16)"),
            inverseJoinColumns = @JoinColumn(
                    name = "ROLE_UUID",
                    referencedColumnName = "UUID",
                    columnDefinition = "BINARY(16)"))
    private Set<Role> roles;

    private Set<Role> getRolesInternal() {
        if(this.roles == null) {
            this.roles = new HashSet<>();
        }
        return this.roles;
    }

    @Override
    public Set<Role> getRoles() {
        Set<Role> roles = new HashSet<>(getRolesInternal());
        return Collections.unmodifiableSet(roles);
    }

    public void addRole(Role role) {
        if(role == null || getRolesInternal().contains(role))
            return;

        getRolesInternal().add(role);
        role.addPerson(this);
    }

    public void removeRole(Role role) {
        if(role == null || !getRolesInternal().contains(role))
            return;

        getRolesInternal().remove(role);
        role.removePerson(this);
    }

    ///////////////// Address (Bi-directional One-to-many) ///////////////////

    @NotAudited
    @Valid
    @NotNull(message = "{message.Person.addresses.null}")
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Address> addresses;

    private Set<Address> getAddressesInternal() {
        if(this.addresses == null)
            this.addresses = new HashSet<>();

        return this.addresses;
    }

    @Override
    public Set<Address> getAddresses() {
        Set<Address> addresses = new HashSet<>(getAddressesInternal());
        return Collections.unmodifiableSet(addresses);
    }

    public void addAddress(Address address) {
        if (address == null || getAddressesInternal().contains(address))
            return;

        getAddressesInternal().add(address);
        address.setPerson(this);
    }

    public void removeAddress(Address address) {
        if(address == null || !getAddressesInternal().contains(address))
            return;

        getAddressesInternal().remove(address);
        address.setPerson(null);
    }

    ///////////////// Phone and Fax (Bi-directional One-to-many)  ///////////////////

    @NotAudited
    @Valid
    @NotNull(message = "{message.Person.phones.null}")
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Phone> phones;


    private Set<Phone> getPhonesInternal() {
        if(this.phones == null)
            this.phones = new HashSet<>();

        return this.phones;
    }

    @Override
    public Set<Phone> getPhones() {
        Set<Phone> phones = new HashSet<>(getPhonesInternal());
        return Collections.unmodifiableSet(phones);
    }

    public void addPhone(Phone phone) {
        if(phone == null || getPhonesInternal().contains(phone))
            return;

        getPhonesInternal().add(phone);
        phone.setPerson(this);
    }

    public void removePhone(Phone phone) {
        if(phone == null || !getPhonesInternal().contains(phone))
            return;

        getPhonesInternal().remove(phone);
        phone.setPerson(null);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Person [");
        sb.append("Username: ").append(this.username).append("; ");
        sb.append("Password: ").append(this.hashedPassword).append("; ");
        sb.append("First name: ").append(this.firstName).append("; ");
        sb.append("Middle name: ").append(this.middleName).append("; ");
        sb.append("Last name: ").append(this.lastName).append("; ");
        sb.append("Email: ").append(this.email).append("; ");
        sb.append("Email confirmed: ").append(this.emailConfirmed).append("; ");
        sb.append("Login enabled: ").append(this.loginEnabled).append("; ");
        sb.append("Account Non-expired: ").append(this.accountNonExpired).append("; ");
        sb.append("Account Non-locked: ").append(this.accountNonLocked).append("; ");
        sb.append("Credentials Non-expired: ").append(this.credentialsNonExpired).append("; ");
        sb.append("Two-factor enabled: ").append(this.twoFactorEnabled).append("; ");
        sb.append("Access failed count: ").append(this.accessFailedCount).append("; ");
        sb.append("Last password reset date: ").append(this.lastPasswordResetDate).append("; ");
        sb.append("Version: ").append(this.version);
        sb.append("]; ");

        sb.append("[Roles empty]; ");
        sb.append("[Addresses empty]; ");
        sb.append("[Phones empty]; ");

        sb.append(super.toString());

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        return getInternalId().equals(((Person) o).getInternalId());
    }

    @Override
    public int hashCode() {
        return getInternalId().hashCode();
    }
}
