package com.bookstore.bookstore_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // ✅ Public endpoints - no authentication required
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/books/all").permitAll()
                        .requestMatchers("/api/books/search/**").permitAll()
                        .requestMatchers("/api/books/paginated").permitAll()
                        .requestMatchers("/api/books/sort/**").permitAll()
                        .requestMatchers("/api/books/new-arrivals").permitAll()
                        .requestMatchers("/error").permitAll()

                        // ✅ Allow Swagger UI access
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        // 🔒 Admin-only endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // 🔒 Vendor-only endpoints
                        .requestMatchers("/api/vendor/**").hasRole("VENDOR")

                        // 🔒 Book management - vendors can add, admins can manage all
                        .requestMatchers("/api/books/add").hasAnyRole("VENDOR", "ADMIN")
                        .requestMatchers("/api/books/*/image").hasAnyRole("VENDOR", "ADMIN")

                        // 🔒 User endpoints (all authenticated users)
                        .requestMatchers("/api/cart/**").hasAnyRole("USER", "ADMIN", "VENDOR")
                        .requestMatchers("/api/orders/**").hasAnyRole("USER", "ADMIN", "VENDOR")
                        .requestMatchers("/api/wishlist/**").hasAnyRole("USER", "ADMIN", "VENDOR")
                        .requestMatchers("/api/addresses/**").hasAnyRole("USER", "ADMIN", "VENDOR")

                        // 🔒 Book rating - authenticated users only
                        .requestMatchers("/api/books/*/rate").hasAnyRole("USER", "ADMIN", "VENDOR")

                        // 🔒 All other requests require authentication
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json");
                            response.setStatus(401);
                            response.getWriter().write(
                                    "{\"success\":false,\"message\":\"Authentication required\",\"timestamp\":\"" +
                                            java.time.LocalDateTime.now() + "\"}"
                            );
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setContentType("application/json");
                            response.setStatus(403);
                            response.getWriter().write(
                                    "{\"success\":false,\"message\":\"Access denied - insufficient privileges\",\"timestamp\":\"" +
                                            java.time.LocalDateTime.now() + "\"}"
                            );
                        })
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
