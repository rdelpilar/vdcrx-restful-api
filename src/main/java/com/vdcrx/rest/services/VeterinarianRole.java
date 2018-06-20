package com.vdcrx.rest.services;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
//@PreAuthorize("hasAuthority(T(com.vdcrx.rest.domain.enums.RoleType).VETERINARIAN.name())")
@PreAuthorize("hasAuthority('VETERINARIAN')")
// https://stackoverflow.com/questions/30788105/spring-security-hasrole-not-working
public @interface VeterinarianRole {
}
