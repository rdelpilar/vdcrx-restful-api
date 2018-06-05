package com.vdcrx.rest.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Person service interface
 *
 * @author Ranel del Pilar
 */

public interface PersonService extends UserDetailsService {

    @Override
    UserDetails loadUserByUsername(final String s) throws UsernameNotFoundException;
}
