package com.homerentsolution.msprecios.config;

import io.swagger.v3.oas.models.OpenAPI;
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
