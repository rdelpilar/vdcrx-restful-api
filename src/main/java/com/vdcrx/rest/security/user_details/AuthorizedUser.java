package com.vdcrx.rest.security.user_details;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;
import java.util.UUID;

/**
 * Authorized user class
 *
 * @author Ranel del Pilar
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizedUser implements UserDetails {
    private UUID id;
    private String username;
    private String password;
    private Set<AuthorizedUserRole> authorities;
    private boolean enabled;
    private long lastPasswordResetDate;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
}
