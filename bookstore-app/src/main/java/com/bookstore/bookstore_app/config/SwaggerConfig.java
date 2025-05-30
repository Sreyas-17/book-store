package com.bookstore.bookstore_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.models.GroupedOpenApi;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi bookstoreApi() {
        return GroupedOpenApi.builder()
                .group("bookstore-api")
                .pathsToMatch("/api/**")
                .build();
    }
}
