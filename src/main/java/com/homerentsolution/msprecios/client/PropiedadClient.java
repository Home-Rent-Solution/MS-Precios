package com.homerentsolution.msprecios.client;

import org.springframework.cloud.openfeign.FeignClient;
import com.homerentsolution.msprecios.dto.PropiedadDTO;
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

    // misma ruta, pero trae el precio por noche para /calcular
    @GetMapping("/api/v1/propiedades/{id}")
    PropiedadDTO obtenerPropiedad(
            @PathVariable Long id
    );
}