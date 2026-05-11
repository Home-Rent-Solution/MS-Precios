package com.homerentsolution.msprecios.controller;

import com.homerentsolution.msprecios.dto.PrecioRequestDTO;
import com.homerentsolution.msprecios.dto.PrecioResponseDTO;
import com.homerentsolution.msprecios.model.Precio;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.homerentsolution.msprecios.service.PrecioService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/precios")

public class PrecioController {

    @Autowired
    private PrecioService service;

    //obtener la lista
    @GetMapping
    public List<Precio> listar() {

        return service.listar();
    }

    //post para guardar en dto y que api devuelve
    @PostMapping
    public PrecioResponseDTO guardar(
            @Valid @RequestBody PrecioRequestDTO dto) {

        return service.guardar(dto);
    }

    //buscar por Id
    @GetMapping("/{id}")
    public Precio buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }



    //eliminar
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }    //actualizar por id
    @PutMapping("/{id}")
    public Precio actualizar(@PathVariable Long id,
                             @RequestBody Precio precio) {

        return service.actualizar(id, precio);
    }

    // Buscar precios por temporada
    @GetMapping("/temporada/{temporada}")
    public List<Precio> buscarPorTemporada(
            @PathVariable String temporada) {

        return service.buscarPorTemporada(temporada);
    }

    // Buscar precios por propiedad
    @GetMapping("/propiedad/{id}")
    public List<Precio> buscarPorPropiedad(
            @PathVariable Long id) {

        return service.buscarPorPropiedad(id);
    }

    // Buscar precios ordenados por multiplicador
    @GetMapping("/temporada/ordenado/{temporada}")
    public List<Precio> buscarPorTemporadaOrdenado(
            @PathVariable String temporada) {

        return service.buscarPorTemporadaOrdenado(
                temporada
        );
    }
}
