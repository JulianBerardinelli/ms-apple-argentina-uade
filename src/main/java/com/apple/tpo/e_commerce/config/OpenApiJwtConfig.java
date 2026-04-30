package com.apple.tpo.e_commerce.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class OpenApiJwtConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME, securityScheme));
    }

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            if (openApi.getPaths() == null) return;

            openApi.getPaths().forEach((path, pathItem) -> {
                if (pathItem == null) return;
                if (isPublicPath(path)) return;

                addSecurityToOperation(pathItem.getGet(), path);
                addSecurityToOperation(pathItem.getPost(), path);
                addSecurityToOperation(pathItem.getPut(), path);
                addSecurityToOperation(pathItem.getDelete(), path);
                addSecurityToOperation(pathItem.getPatch(), path);
            });
        };
    }

    private void addSecurityToOperation(Operation operation, String path) {
        if (operation == null) return;

        // Si ya tiene seguridad, no duplicamos
        if (operation.getSecurity() != null && !operation.getSecurity().isEmpty()) return;

        operation.addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
    }

    private boolean isPublicPath(String path) {
        // Endpoints que ya están permitidos en SecurityConfig
        if (path == null) return false;
        return path.startsWith("/api/auth/")
                || Objects.equals(path, "/api/auth/login")
                || Objects.equals(path, "/api/auth/register")
                || path.startsWith("/actuator/health")
                || path.startsWith("/swagger-ui/")
                || path.startsWith("/v3/api-docs");
    }
}

