package com.ojashwa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity(debug = true)
/**
 * Completely optional to use these annotations within spring-boot environment,
 * it was required in spring-core-framework.
 *
 * @EnableWebSecurity
 * @EnableJpaRepositories("com.ojashwa.repository")
 * @EntityScan("com.ojashwa.model")
 */
public class SpringSecurityBankBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityBankBackendApplication.class, args);
    }
}