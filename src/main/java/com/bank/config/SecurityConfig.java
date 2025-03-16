package com.bank.config;

import com.bank.controller.auth.UserDetailsServiceImpl;
import com.bank.controller.filter.JwtRequestFilter;
import com.bank.domain.model.Role;
import com.bank.util.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtRequestFilter jwtRequestFilter) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);

        // Use JWT token based auth
        http.securityMatcher("/api/**", "/ping", "/deep-ping")
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/auth", "/ping", "/deep-ping").permitAll()
                        .requestMatchers("/api/**").authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);


        // Use default session-based auth
        http.securityMatcher("/web/**", "/login", "/logout", "/*.css", "/*.js")
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/web/home/**").hasRole(Role.CUSTOMER.toString())
                        .requestMatchers("/web/ops/**").hasRole(Role.OPERATION.toString())
                        .requestMatchers("/web/admin/**").hasRole(Role.ADMIN.toString())
                        .requestMatchers("/login", "/logout", "/error", "/*.css", "/*.js").permitAll()
                        .requestMatchers("/web/**").authenticated())
                .formLogin(form -> form.defaultSuccessUrl("/web/home"))
                .logout(logout -> logout.logoutSuccessUrl("/login"))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                );;


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Usually Spring Security creates AuthenticationManager automatically unless we overrides default config?
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService(AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder) throws Exception {
        var userDetailsService = new UserDetailsServiceImpl(passwordEncoder);
        auth.userDetailsService(userDetailsService);
        return userDetailsService;
    }

    @Bean
    public JwtRequestFilter jwtRequestFilter(UserDetailsService userDetailsService, JwtHelper jwtHelper) {
        return new JwtRequestFilter(userDetailsService, jwtHelper);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}