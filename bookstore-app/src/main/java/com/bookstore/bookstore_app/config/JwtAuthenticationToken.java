package com.bookstore.bookstore_app.config;

import com.bookstore.bookstore_app.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final Long userId;
    private final User.Role role;

    public JwtAuthenticationToken(String email, Long userId, User.Role role, 
                                Collection<? extends GrantedAuthority> authorities) {
        super(email, null, authorities);
        this.userId = userId;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public User.Role getRole() {
        return role;
    }

    public String getEmail() {
        return (String) getPrincipal();
    }
}