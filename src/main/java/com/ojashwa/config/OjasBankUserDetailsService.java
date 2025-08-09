package com.ojashwa.config;

import com.ojashwa.model.Authority;
import com.ojashwa.model.Customer;
import com.ojashwa.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class OjasBankUserDetailsService implements UserDetailsService {

    /**
     * @Autowired It's not necessary to autowire the dependency when we only have one dependency in our class
     * Just @RequiredArgsConstructor will tell spring boot to autowire the dependency.
     */
    private final CustomerRepository customerRepository;

    /**
     * @param username the username identifying the user whose data is required.
     * @return UserDetails object from the configured database
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User details not found for the user: " + username));
        List<SimpleGrantedAuthority> authorities = customer.getAuthorities().stream()
                .map(authority ->
                        new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());
        /*
          * Without "authorities" table we had to manually set up simple granted authority from the customer table column value "role"
          * List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(customer.getRole()));
         */
        return new User(customer.getEmail(), customer.getPwd(), authorities);
    }
}