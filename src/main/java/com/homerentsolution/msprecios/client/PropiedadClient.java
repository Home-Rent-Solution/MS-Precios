package com.homerentsolution.msprecios.client;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "ms-propiedades"
)
public interface PropiedadClient {

    @GetMapping("/api/v1/propiedades/{id}")
    Object buscarPorId(
            @PathVariable Long id
    );
}