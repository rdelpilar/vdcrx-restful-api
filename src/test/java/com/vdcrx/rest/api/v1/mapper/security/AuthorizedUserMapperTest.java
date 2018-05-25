package com.vdcrx.rest.api.v1.mapper.security;

import com.vdcrx.rest.api.v1.model.dto.JwtAuthorizedUserDto;
import com.vdcrx.rest.domain.entities.Person;
import com.vdcrx.rest.domain.entities.Role;
import com.vdcrx.rest.domain.entities.Veterinarian;
import com.vdcrx.rest.domain.enums.RoleType;
import com.vdcrx.rest.security.user_details.AuthorizedUser;
import com.vdcrx.rest.security.user_details.AuthorizedUserRole;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AuthorizedUserMapperTest.AuthorizedUserMapperTestConfig.class)
public class AuthorizedUserMapperTest {

    @Configuration
    @ComponentScan(basePackageClasses = AuthorizedUserMapperTest.class)
    public static class AuthorizedUserMapperTestConfig {}

    @Autowired
    private AuthorizedUserMapper authorizedUserMapper;

    private final RoleType roleType = RoleType.VETERINARIAN;

    private final String username = "peter";
    private final Set<AuthorizedUserRole> roles = new HashSet<>();
    private final boolean enabled = false;
    private final long lastPasswordResetDate = Instant.now().toEpochMilli();
    private final boolean accountNonExpired = true;
    private final boolean accountNonLocked = true;
    private final boolean credentialsNonExpired = true;

    private final UUID uuid2 = UUID.randomUUID();
    private final String username2 = "harold";
    private final String password2 = "$2a$10$AZV34PDOUtKYCXhwJ96z8.3MZgQjB7b27kjLdf7f9apGK9GMQ/jkK";
    private final boolean enabled2 = false;
    private final long lastPasswordResetDate2 = Instant.now().toEpochMilli();
    private final boolean accountNonExpired2 = true;
    private final boolean accountNonLocked2 = true;
    private final boolean credentialsNonExpired2 = true;

    @Test
    public void mapToAuthorizedUserRole() {
        // Given

        Role source = new Role();
        source.setName(roleType);

        // When
        AuthorizedUserRole target = authorizedUserMapper.mapToAuthorizedUserRole(source);

        // Then
        assertNotNull(target);
        assertEquals(RoleType.VETERINARIAN, target.getRoleType());
    }

    @Test
    public void mapToJwtUserDto() {
        // Given
        AuthorizedUser authorizedUser = new AuthorizedUser();

        authorizedUser.setUsername(username);
        roles.add(new AuthorizedUserRole(RoleType.VETERINARIAN));
        roles.add(new AuthorizedUserRole(RoleType.PET_OWNER));
        authorizedUser.setAuthorities(roles);
        authorizedUser.setEnabled(enabled);
        authorizedUser.setLastPasswordResetDate(lastPasswordResetDate);
        authorizedUser.setAccountNonExpired(accountNonExpired);
        authorizedUser.setAccountNonLocked(accountNonLocked);
        authorizedUser.setCredentialsNonExpired(credentialsNonExpired);

        // When
        JwtAuthorizedUserDto dto = authorizedUserMapper.mapToJwtUserDto(authorizedUser);

        // Then
        assertNotNull(dto);
        assertTrue(dto.getAuthorities().stream().anyMatch(role -> "VETERINARIAN".equals((role).getAuthority())));
        assertTrue(dto.getAuthorities().stream().anyMatch(role -> "PET_OWNER".equals((role).getAuthority())));
        assertFalse(dto.isEnabled());
        assertTrue(dto.isAccountNonExpired());
        assertTrue(dto.isAccountNonLocked());
        assertTrue(dto.isCredentialsNonExpired());
    }

    @Test
    public void mapToAuthorizedUser() {
        // Given
        Person source = new Veterinarian();

        source.setId(uuid2);
        source.setUsername(username2);
        source.setHashedPassword(password2);
        source.addRole(new Role(RoleType.PET_OWNER));
        source.addRole(new Role(RoleType.VETERINARIAN));
        source.setLoginEnabled(enabled2);
        source.setLastPasswordResetDate(lastPasswordResetDate2);
        source.setAccountNonExpired(accountNonExpired2);
        source.setAccountNonLocked(accountNonLocked2);
        source.setCredentialsNonExpired(credentialsNonExpired2);

        // When
        AuthorizedUser target = authorizedUserMapper.mapToAuthorizedUser(source);

        // Then
        assertNotNull(target);
        assertEquals(uuid2, target.getId());
        assertEquals("harold", target.getUsername());
        assertEquals(password2, target.getPassword());
        assertTrue(target.getAuthorities().stream().anyMatch(role -> "PET_OWNER".equals((role).getAuthority())));
        assertTrue(target.getAuthorities().stream().anyMatch(role -> "VETERINARIAN".equals((role).getAuthority())));
        assertFalse(target.isEnabled());
        assertEquals(lastPasswordResetDate2, target.getLastPasswordResetDate());
        assertTrue(target.isAccountNonExpired());
        assertTrue(target.isAccountNonLocked());
        assertTrue(target.isCredentialsNonExpired());
    }
}