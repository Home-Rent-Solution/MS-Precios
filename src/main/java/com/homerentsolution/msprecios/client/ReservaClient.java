package com.homerentsolution.msprecios.client;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "ms-reservas"
)
public interface ReservaClient {

    @GetMapping("/reservas/{id}/cliente")
    Object buscarReserva(
            @PathVariable int id
    );
}