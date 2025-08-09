package com.ojashwa.config;

import com.ojashwa.exceptionhandling.CustomAccessDeniedHandler;
import com.ojashwa.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import com.ojashwa.filter.AuthoritiesLoggingAtFilter;
import com.ojashwa.filter.AuthoritiesLogginngAfterFilter;
import com.ojashwa.filter.CsrfCookieFilter;
import com.ojashwa.filter.RequestValidationBeforeFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@Profile("!prod")
public class ProjectSecurityConfiguration {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
/*
        http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
        http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());
        http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());
*/

        // This class allows our backend application to be able to read the csrf-token from the request header / parameter.
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();

        http.securityContext(contextConfig -> contextConfig.requireExplicitSave(false))
                // By this we are telling spring security to save the jsessionid cookie in the security context
                .sessionManagement(sessionConfig -> sessionConfig
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setMaxAge(3600L);
                        return config;
                    }
                }))
                .csrf(csrfConfigurer -> csrfConfigurer.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                        .ignoringRequestMatchers("/contact", "/register")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new AuthoritiesLogginngAfterFilter(), BasicAuthenticationFilter.class)
                .addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class)
                .sessionManagement(smc -> smc.invalidSessionUrl("/invalidSession")
                        .maximumSessions(3).maxSessionsPreventsLogin(true))
                .requiresChannel(rcc -> rcc.anyRequest().requiresInsecure()) // Only HTTP
                .authorizeHttpRequests((requests) -> requests
//                        .requestMatchers("/myAccount", "/myBalance", "/myCard", "/myLoans", "/user").authenticated()
/*                        .requestMatchers("/myAccount").hasAuthority("VIEWACCOUNT")
                        .requestMatchers( "/myBalance").hasAnyAuthority("VIEWBALANCE", "VIEWACCOUNT")
                        .requestMatchers("/myCards").hasAuthority("VIEWCARDS")
                        .requestMatchers( "/myLoans").hasAuthority("VIEWLOANS")*/
                        .requestMatchers("/myAccount").hasRole("USER")
                        .requestMatchers( "/myBalance").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/myCards").hasRole("USER")
                        .requestMatchers( "/myLoans").hasRole("USER")
                        .requestMatchers("/user").authenticated()
                        .requestMatchers("/notices", "/contact", "/error", "/register", "/invalidSession").permitAll());
        http.formLogin(flc -> flc.disable());
        http.httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
        return http.build();
    }


   /* @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }
   */

    // InMemoryUserDetailsManager implementation class of UserDetailsManager interface
/*    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user").password("{noop}OjashwaTripathi@12345").authorities("read").build();
        UserDetails admin = User.withUsername("admin")
                .password("{bcrypt}$2a$12$IQwLZTPdxUf0nKMSiCCMmu8DJv.V4Z/vUO/MsIP6.nC.0Hg17hXay")
                .authorities("admin").build();

        * One more way to create a user but its less readable in comparison to builder() method.
        * UserDetails testUser = new User("testUser","{noop}TestUser@1001", List.of(new SimpleGrantedAuthority("read")));
        return new InMemoryUserDetailsManager(user, admin);
    }
*/

    @Bean
    public PasswordEncoder passwordEncoder() {
        /**
         * return new BCryptPasswordEncoder();
         * Completely fine to use an instance of BcryptPasswordEncoder - default password encoder for PasswordEncoderFactories class.
         * but down the line, in the future, may be a more advanced and reliable password encoder is available,
         * then using the following method seems like a wise choice, as it provides us with flexibility.
         */
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * Introduced in Spring Security 6.3
     * Highly recommended for production use.
     */

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}