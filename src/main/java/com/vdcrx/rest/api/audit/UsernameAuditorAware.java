package com.vdcrx.rest.api.audit;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class UsernameAuditorAware implements AuditorAware<String> {

    // NEED TO MODIFY THIS USING:
    // https://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-auditing-part-two/
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("ACCOUNT_USERNAME_HERE");
    }
}
