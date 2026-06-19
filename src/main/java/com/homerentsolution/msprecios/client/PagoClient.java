package com.homerentsolution.msprecios.client;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "ms-pagos"
)
public interface PagoClient {

    @GetMapping("/api/v1/pagos/{id}")
    Object buscarPago(
            @PathVariable Long id
    );
}