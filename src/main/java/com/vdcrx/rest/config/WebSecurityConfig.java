package com.vdcrx.rest.config;

import com.vdcrx.rest.security.JwtTokenUtil;
import com.vdcrx.rest.security.entry_points.AuthenticationFilterEntryPoint;
import com.vdcrx.rest.security.entry_points.DefaultAuthenticationEntryPoint;
import com.vdcrx.rest.security.filters.AuthenticationFilter;
import com.vdcrx.rest.security.filters.AuthorizationFilter;
import com.vdcrx.rest.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static com.vdcrx.rest.constants.ApiConstants.LOGIN_PATH;
import static com.vdcrx.rest.constants.ApiConstants.VET_SIGNUP_PATH;

/**
 * Web security configuration
 *
 * Contains beans used for security
 *
 * @author Ranel del Pilar
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@Order(1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private PersonService personService;
    private PasswordEncoder passwordEncoder;

    //    @Autowired
    //    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    public WebSecurityConfig(PersonService personService,
                             PasswordEncoder passwordEncoder) {
        super();
        this.personService = personService;
        this.passwordEncoder = passwordEncoder;
    }
    @Bean
    public AuthenticationManager getAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Bean
    public JwtTokenUtil getJwtTokenUtil() {
        return new JwtTokenUtil();
    }

    @Bean
    public AuthenticationFilter getAuthenticationFilter() throws Exception {
        return new AuthenticationFilter(authenticationManager(), getAuthenticationFilterEntryPoint());
    }

    @Bean
    public AuthorizationFilter getAuthorizationFilter() {
        return new AuthorizationFilter();
    }

    @Bean
    public AuthenticationFilterEntryPoint getAuthenticationFilterEntryPoint() {
        return new AuthenticationFilterEntryPoint();
    }

    @Bean
    public DefaultAuthenticationEntryPoint getDefaultAuthenticationEntryPoint() {
        return new DefaultAuthenticationEntryPoint();
    }

    @Override
    public void configure(WebSecurity web) {

        web
                .ignoring()
                .antMatchers(HttpMethod.POST,
                        VET_SIGNUP_PATH);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http

                .cors().and().csrf().disable()

                .exceptionHandling().authenticationEntryPoint(getDefaultAuthenticationEntryPoint())

                // handler is used for the following:
                // @PreAuthorized, @PostAuthorized, @Secured
                //
                //.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler)
                .and()

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()

                .authorizeRequests()

                .antMatchers(HttpMethod.POST, LOGIN_PATH).permitAll()

                .anyRequest().authenticated()

                .and()

                .addFilter(getAuthenticationFilter())

                .addFilterBefore(getAuthorizationFilter(), BasicAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth
                .userDetailsService(personService)
                .passwordEncoder(passwordEncoder);
    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        LOG.info("Setting corConfigurationSource");
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
//        return source;
//    }
}
