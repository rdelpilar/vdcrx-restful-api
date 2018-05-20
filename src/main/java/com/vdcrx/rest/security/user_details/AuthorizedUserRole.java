package com.vdcrx.rest.security.user_details;

import com.vdcrx.rest.domain.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

/**
 * Authorized user role class
 *
 * @author Ranel del Pilar
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizedUserRole implements GrantedAuthority {

    private RoleType roleType;

    @Override
    public String getAuthority() {
        return roleType.name();
    }
}
