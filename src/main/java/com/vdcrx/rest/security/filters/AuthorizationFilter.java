package com.vdcrx.rest.security.filters;

import com.vdcrx.rest.exceptions.controller_advice.RestResponseExceptionHandler;
import com.vdcrx.rest.security.JwtTokenUtil;
import com.vdcrx.rest.security.user_details.AuthorizedUser;
import com.vdcrx.rest.services.PersonService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.vdcrx.rest.constants.ApiConstants.LOGIN_PATH;
import static com.vdcrx.rest.constants.SecurityConstants.HEADER_AUTH;
import static com.vdcrx.rest.constants.SecurityConstants.HEADER_BEARER;

@NoArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestResponseExceptionHandler exceptionHandler;

    @Autowired
    private PersonService personService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public AuthorizationFilter(RestResponseExceptionHandler exceptionHandler,
                               PersonService personService,
                               JwtTokenUtil jwtTokenUtil) {
        this.exceptionHandler = exceptionHandler;
        this.personService = personService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String method = request.getMethod();

        switch (method) {
            case "GET":
            case "POST":
            case "PUT":
            case "PATCH":
            case "DELETE":
                doFilterMethod(request, response, chain);
                break;
            default:
                this.exceptionHandler
                        .handleHttpRequestMethodNotSupported(
                                new HttpRequestMethodNotSupportedException(method), request, response);
                break;
        }
    }

    private void doFilterMethod(HttpServletRequest request,
                                HttpServletResponse response,
                                FilterChain chain) throws ServletException, IOException {

        final String header = request.getHeader(HEADER_AUTH);

        if(header != null && header.startsWith(HEADER_BEARER) && !request.getRequestURI().equals(LOGIN_PATH)) {

            LOG.debug("Processing authorization for URL '{}'", request.getRequestURI());

            try {

                String authToken = header.substring(7);
                String username = jwtTokenUtil.getUsernameFromToken(authToken);

                if (username != null) {

                    // loadUserByUsername is Cacheable
                    final AuthorizedUser authorizedUser = (AuthorizedUser) this.personService.loadUserByUsername(username);

                    if(jwtTokenUtil.validateToken(authToken, authorizedUser)) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(authorizedUser, null, authorizedUser.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        LOG.debug("Authorized user '{}', setting security context", username);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        LOG.error("JWT validation error, token is created after last password reset date");
                        this.exceptionHandler
                                .handleSignatureException(new SignatureException("Invalid authorization token, please log in again"), request, response);
                    }
                }
            } catch (IllegalArgumentException ex) {
                this.exceptionHandler.handleIllegalArgumentException(ex, request, response);
            } catch (ExpiredJwtException ex) {
                this.exceptionHandler.handleExpiredJwtException(ex, request, response);
            } catch (UnsupportedJwtException ex) {
                this.exceptionHandler.handleUnsupportedJwtException(ex, request, response);
            } catch (MalformedJwtException ex) {
                this.exceptionHandler.handleMalformedJwtException(ex, request, response);
            } catch (SignatureException ex) {
                this.exceptionHandler.handleSignatureException(ex, request, response);
            }
        }

        chain.doFilter(request, response);
    }
}
