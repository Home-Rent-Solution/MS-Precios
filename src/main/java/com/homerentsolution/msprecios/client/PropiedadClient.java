package com.homerentsolution.msprecios.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "ms-propiedades",
        url = "http://localhost:8081"
)
public interface PropiedadClient {

    @GetMapping("/api/v1/propiedades/{id}")
    Object buscarPropiedad(
            @PathVariable Long id
    );
}