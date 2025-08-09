package com.ojashwa.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("!prod")
@RequiredArgsConstructor
public class OjasBankUsernamePwdAuthenticationProvider implements AuthenticationProvider {


    private final OjasBankUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    /**
     * This method performs the actual authentication based on the provided username and password.
     * <p>
     * It first loads the user details using the provided username from the {@link OjasBankUserDetailsService}.
     * Then it checks if the provided password matches the stored password using the {@link PasswordEncoder}.
     * <p>
     * If the password matches, it returns a new {@link UsernamePasswordAuthenticationToken} with the username, password,
     * and the authorities of the user.
     * <p>
     * If the password doesn't match, it throws a {@link BadCredentialsException}.
     * @param authentication the authentication object containing the username and password
     * @return the authenticated object
     * @throws AuthenticationException if the authentication fails
     */

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // Since it's not a production environment profile, we are not matching the password
        // and simply authenticate the logged-in user
        return new UsernamePasswordAuthenticationToken(username, pwd, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
