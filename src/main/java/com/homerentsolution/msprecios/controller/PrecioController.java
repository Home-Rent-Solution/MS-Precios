package com.homerentsolution.msprecios.controller;

import com.homerentsolution.msprecios.dto.PrecioRequestDTO;
import com.homerentsolution.msprecios.dto.PrecioResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.homerentsolution.msprecios.service.PrecioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/v1/precios")

// Agrupa todos los endpoints de precios en una sección de Swagger
@Tag(
        name = "Precios",
        description = "Operaciones relacionadas con precios y temporadas"
)

public class PrecioController {

    //Inyectar automaticamente las dependencias mediante constructor
    private final PrecioService service;

    public PrecioController(PrecioService service) {
        this.service = service;
    }

    private static final Logger log =
            LoggerFactory.getLogger(PrecioController.class);


    // Documenta el propósito del endpoint en Swagger
    @Operation(
            summary = "Listar precios",
            description = "Obtiene todos los precios registrados"
    )
    // Documenta las posibles respuestas HTTP del endpoint
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Precios obtenidos correctamente"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    //listartodo con ResponseEntity reponde 200 ok
    @GetMapping
    public ResponseEntity<List<PrecioResponseDTO>> listar() {

        log.info("Listando todos los precios");

        return ResponseEntity.ok(service.listar());
    }

    @Operation(
            summary = "Crear precio",
            description = "Registra un nuevo precio"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Precio creado correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            )
    })
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

    @Operation(
            summary = "Buscar precio",
            description = "Obtiene un precio por su identificador"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Precio encontrado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Precio no encontrado"
            )
    })
    //buscar por Id, en responseEntity responde 200 ok
    @GetMapping("/{id}")
    public ResponseEntity<PrecioResponseDTO> buscar(
            @PathVariable Long id) {

        log.info("Buscando precio con ID: {}", id);

        return ResponseEntity.ok(
                service.buscarPorId(id));
    }

    @Operation(
            summary = "Eliminar precio",
            description = "Elimina un precio existente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Precio eliminado correctamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Precio no encontrado"
            )
    })
    //eliminar, responseidentity devuelve 204  no content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        log.warn("Eliminando precio con ID: {}", id);

        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Actualizar precio",
            description = "Actualiza un precio existente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Precio actualizado correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Precio no encontrado"
            )
    })
    //actualizar por id con response entity
    @PutMapping("/{id}")
    public ResponseEntity<PrecioResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PrecioRequestDTO dto) {

        log.info("Actualizando precio con ID: {}", id);

        PrecioResponseDTO actualizado =
                service.actualizar(id, dto);

        return ResponseEntity.ok(actualizado);
    }

    @Operation(
            summary = "Buscar por temporada",
            description = "Obtiene todos los precios asociados a una temporada"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Precios encontrados"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existen precios para la temporada"
            )
    })
    // Buscar precios por temporada
    @GetMapping("/temporada/{temporada}")
    public ResponseEntity<List<PrecioResponseDTO>> buscarPorTemporada(
            @PathVariable String temporada) {
        log.info(
                "Buscando precios para temporada: {}",
                temporada
        );

        return ResponseEntity.ok(
                service.buscarPorTemporada(temporada));
    }

    @Operation(
            summary = "Buscar por propiedad",
            description = "Obtiene todos los precios asociados a una propiedad"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Precios encontrados"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existen precios para la propiedad"
            )
    })
    // Buscar precios por propiedad
    @GetMapping("/propiedad/{id}")
    public ResponseEntity<List<PrecioResponseDTO>> buscarPorPropiedad(
            @PathVariable Long id) {
        log.info(
                "Buscando precios para propiedad con ID: {}",
                id
        );

        return ResponseEntity.ok(
                service.buscarPorPropiedad(id));
    }

    @Operation(
            summary = "Buscar precios ordenados",
            description = "Obtiene los precios de una temporada ordenados por multiplicador descendente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Precios encontrados"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existen precios para la temporada"
            )
    })
    // Buscar precios ordenados por multiplicador
    @GetMapping("/temporada/ordenado/{temporada}")
    public ResponseEntity<List<PrecioResponseDTO>> buscarPorTemporadaOrdenado(
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

