package com.bookstore.bookstore_app.config;

import com.bookstore.bookstore_app.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LogManager.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestPath = request.getRequestURI();
        logger.info("JWT Filter processing: {} {}", request.getMethod(), requestPath);

        if (isPublicEndpoint(requestPath)) {
            logger.info("Skipping JWT validation for public endpoint: {}", requestPath);
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            email = jwtUtil.getEmailFromToken(token);
            logger.info("Extracted email from token: {}", email);
        } else {
            logger.warn("No Authorization header found or invalid format");
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateToken(token) && !jwtUtil.isTokenExpired(token)) {

                // Get role from token
                User.Role role = jwtUtil.getRoleFromToken(token);
                Long userId = jwtUtil.getUserIdFromToken(token);

                logger.info("Token valid for user: {} with role: {} and ID: {}", email, role, userId);

                // Create authority based on role
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.name());

                // Create authentication token
                // Create JWT authentication token with userId and role
                JwtAuthenticationToken authToken = new JwtAuthenticationToken(
                        email,
                        userId,
                        role,
                        Collections.singletonList(authority)
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);

                logger.info("Authentication successful for user: {} with role: {}", email, role);
            } else {
                logger.warn("Invalid or expired token for user: {}", email);
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(String requestPath) {
        return requestPath.equals("/api/auth/register") ||
                requestPath.equals("/api/auth/register-vendor") ||
                requestPath.equals("/api/auth/register-admin") ||
                requestPath.equals("/api/auth/login") ||
                requestPath.equals("/api/auth/forgot-password") ||
                requestPath.equals("/api/books/all") ||
                requestPath.startsWith("/api/books/search/") ||
                requestPath.startsWith("/api/books/paginated") ||
                requestPath.startsWith("/api/books/sort/") ||
                requestPath.startsWith("/api/books/new-arrivals") ||
                requestPath.startsWith("/error") ||
                requestPath.equals("/");
    }
}