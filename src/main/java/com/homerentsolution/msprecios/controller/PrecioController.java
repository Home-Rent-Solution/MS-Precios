package com.homerentsolution.msprecios.controller;

import com.homerentsolution.msprecios.dto.PrecioRequestDTO;
import com.homerentsolution.msprecios.dto.PrecioResponseDTO;
import com.homerentsolution.msprecios.model.Precio;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.homerentsolution.msprecios.service.PrecioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/precios")

public class PrecioController {

    //Inyectar automaticamente las dependencias
    @Autowired
    private PrecioService service;

    private static final Logger log =
            LoggerFactory.getLogger(PrecioController.class);


    //listartodo con ResponseEntity reponde 200 ok
    @GetMapping
    public ResponseEntity<List<Precio>> listar() {

        log.info("Listando todos los precios");

        return ResponseEntity.ok(service.listar());
    }

    //post para guardar en dto y que api- devuelve 201 created
    @PostMapping
    public ResponseEntity<PrecioResponseDTO> guardar(
            @Valid @RequestBody PrecioRequestDTO dto) {

        log.info(
                "Guardando precio para propiedad {} en temporada {}",
                dto.getIdPropiedad(),
                dto.getTemporada()
        );

        PrecioResponseDTO respuesta = service.guardar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(respuesta);
    }

    //buscar por Id, en responseEntity responde 200 ok
    @GetMapping("/{id}")
    public ResponseEntity<Precio> buscar(
            @PathVariable Long id) {

        log.info("Buscando precio con ID: {}", id);

        return ResponseEntity.ok(
                service.buscarPorId(id));
    }


    //eliminar, responseidentity devuelve 204  no content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        log.warn("Eliminando precio con ID: {}", id);

        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    //actualizar por id con response entity
    @PutMapping("/{id}")
    public ResponseEntity<Precio> actualizar(
            @PathVariable Long id,
            @RequestBody Precio precio) {

        log.info("Actualizando precio con ID: {}", id);

        Precio actualizado =
                service.actualizar(id, precio);

        return ResponseEntity.ok(actualizado);
    }

    // Buscar precios por temporada
    @GetMapping("/temporada/{temporada}")
    public ResponseEntity<List<Precio>> buscarPorTemporada(
            @PathVariable String temporada) {
        log.info(
                "Buscando precios para temporada: {}",
                temporada
        );

        return ResponseEntity.ok(
                service.buscarPorTemporada(temporada));
    }

    // Buscar precios por propiedad
    @GetMapping("/propiedad/{id}")
    public ResponseEntity<List<Precio>> buscarPorPropiedad(
            @PathVariable Long id) {
        log.info(
                "Buscando precios para propiedad con ID: {}",
                id
        );

        return ResponseEntity.ok(
                service.buscarPorPropiedad(id));
    }

    // Buscar precios ordenados por multiplicador
    @GetMapping("/temporada/ordenado/{temporada}")
    public ResponseEntity<List<Precio>> buscarPorTemporadaOrdenado(
            @PathVariable String temporada) {
        log.info(
                "Buscando precios ordenados para temporada: {}",
                temporada
        );

        return ResponseEntity.ok(
                service.buscarPorTemporadaOrdenado(
                        temporada
                )
        );
    }
}

