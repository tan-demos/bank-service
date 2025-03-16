package com.bank.controller.auth;

import com.bank.domain.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

public class UserDetailsServiceImpl implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;

    public UserDetailsServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            var role = Role.valueOf(username.toUpperCase());
            var roles = new String[role.getValue()+1];
            for (int i = 0; i < roles.length; i++) {
                roles[i] = Role.fromValue(i).toString();
            }

            return User.withUsername(username)
                    .password(passwordEncoder.encode("123"))
                    .roles(roles)
                    .build();
        } catch (IllegalArgumentException e) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
}
