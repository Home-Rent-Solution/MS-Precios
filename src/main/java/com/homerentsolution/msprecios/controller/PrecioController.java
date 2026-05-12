package com.homerentsolution.msprecios.controller;

import com.homerentsolution.msprecios.dto.PrecioRequestDTO;
import com.homerentsolution.msprecios.dto.PrecioResponseDTO;
import com.homerentsolution.msprecios.model.Precio;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.homerentsolution.msprecios.service.PrecioService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/precios")

public class PrecioController {

    @Autowired
    private PrecioService service;

    //obtener la lista con responseEntity
    @GetMapping
    public ResponseEntity<List<Precio>> listar() {

        return ResponseEntity.ok(service.listar());
    }

    //post para guardar en dto y que api- devuelve 201 created
    @PostMapping
    public ResponseEntity<PrecioResponseDTO> guardar(
            @Valid @RequestBody PrecioRequestDTO dto) {

        PrecioResponseDTO respuesta = service.guardar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(respuesta);
    }

    //buscar por Id con respuesta entity
    @GetMapping("/{id}")
    public ResponseEntity<Precio> buscar(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.buscarPorId(id));
    }


    //eliminar responseidentity
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    //actualizar por id con response entity
    @PutMapping("/{id}")
    public ResponseEntity<Precio> actualizar(
            @PathVariable Long id,
            @RequestBody Precio precio) {

        Precio actualizado =
                service.actualizar(id, precio);

        return ResponseEntity.ok(actualizado);
    }

    // Buscar precios por temporada
    @GetMapping("/temporada/{temporada}")
    public ResponseEntity<List<Precio>> buscarPorTemporada(
            @PathVariable String temporada) {

        return ResponseEntity.ok(
                service.buscarPorTemporada(temporada));
    }

    // Buscar precios por propiedad
    @GetMapping("/propiedad/{id}")
    public ResponseEntity<List<Precio>> buscarPorPropiedad(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.buscarPorPropiedad(id));
    }

    // Buscar precios ordenados por multiplicador
    @GetMapping("/temporada/ordenado/{temporada}")
    public ResponseEntity<List<Precio>> buscarPorTemporadaOrdenado(
            @PathVariable String temporada) {

        return ResponseEntity.ok(
                service.buscarPorTemporadaOrdenado(
                        temporada
                )
        );
    }
}

