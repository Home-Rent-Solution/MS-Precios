package com.homerentsolution.msprecios.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//Configuracion general de Swagger/OpenApi
public class OpenApiConfig {

    // Define la información visible en Swagger UI
    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .info(
                        new Info()
                                .title("HomeRentSolution - API Precios")
                                .version("v1")
                                .description(
                                        "Microservicio encargado de la gestión de precios y temporadas."
                                )
                );
    }
}
