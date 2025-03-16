package com.bank.controller.api;

import com.bank.api.model.AuthInfo;
import com.bank.api.model.AuthRequest;
import com.bank.api.model.AuthResponse;
import com.bank.util.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class JwtAuthController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    private final JwtHelper jwtHelper;

    @Autowired
    public JwtAuthController(AuthenticationManager authenticationManager,
                             UserDetailsService userDetailsService,
                             JwtHelper jwtHelper) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtHelper = jwtHelper;
    }

    @PostMapping("/auth")
    public AuthResponse auth(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        var token = jwtHelper.generateToken(userDetails);
        return new AuthResponse().token(token);
    }

    @GetMapping("/auth-info")
    public AuthInfo getAuthInfo() {
        var name = SecurityContextHolder.getContext().getAuthentication().getName();
        return new AuthInfo().username(name);
    }
}
