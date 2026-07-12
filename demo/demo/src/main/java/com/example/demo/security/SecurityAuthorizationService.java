package com.example.demo.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Objects;

@Component("securityAuthorizationService")
public class SecurityAuthorizationService {

    public boolean canAccessAdminUsers(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        boolean hasAdminRole = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).filter(Objects::nonNull)
                .anyMatch(authority -> authority.equals("ROLE_ADMIN"));

        String username = authentication.getName();
        boolean gmailUser = username != null && username.toLowerCase(Locale.ROOT).endsWith("@gmail.com");

        return hasAdminRole && gmailUser;
    }
}
