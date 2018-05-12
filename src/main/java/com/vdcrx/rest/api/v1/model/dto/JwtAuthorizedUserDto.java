package com.vdcrx.rest.api.v1.model.dto;

import com.vdcrx.rest.security.user_details.AuthorizedUserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

/**
 * Data transfer object mapper for JWT User object
 *
 * @author Ranel del Pilar
 */

@Getter
@Setter
@NoArgsConstructor
public class JwtAuthorizedUserDto {
    private UUID id;
    private Set<AuthorizedUserRole> authorities;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
}
