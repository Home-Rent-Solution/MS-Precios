package com.homerentsolution.msprecios.service;

import com.homerentsolution.msprecios.dto.PrecioRequestDTO;
import com.homerentsolution.msprecios.dto.PrecioResponseDTO;
import com.homerentsolution.msprecios.model.Precio;
import com.homerentsolution.msprecios.repository.PrecioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrecioService {

    @Autowired
    private PrecioRepository repository;

    // LISTAR
    public List<Precio> listar() {

        return repository.findAll();
    }

    // GUARDAR en dto
    public PrecioResponseDTO guardar(PrecioRequestDTO dto) {

        // Convertir DTO a Entity
        Precio precio = new Precio();

        precio.setTemporada(dto.getTemporada());
        precio.setMultiplicador(dto.getMultiplicador());
        precio.setIdPropiedad(dto.getIdPropiedad());

        // Regla de negocio
        //multiplicador mayor a 0
        if (precio.getMultiplicador() <= 0) {

            throw new RuntimeException(
                    "El multiplicador debe ser mayor a 0"
            );
        }

        Precio guardado = repository.save(precio);

        // Convertir Entity a ResponseDTO
        PrecioResponseDTO response =
                new PrecioResponseDTO();

        response.setIdPrecios(
                guardado.getIdPrecios()
        );

        response.setTemporada(
                guardado.getTemporada()
        );

        response.setMultiplicador(
                guardado.getMultiplicador()
        );

        response.setIdPropiedad(
                guardado.getIdPropiedad()
        );

        return response;
    }

    // BUSCAR POR ID
    public Precio buscarPorId(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Precio no encontrado"
                        ));
    }

    // ACTUALIZAR
    public Precio actualizar(Long id,
                             Precio precioActualizado) {

        Precio precio = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Precio no encontrado"
                        ));

        precio.setTemporada(
                precioActualizado.getTemporada()
        );

        precio.setMultiplicador(
                precioActualizado.getMultiplicador()
        );

        precio.setIdPropiedad(
                precioActualizado.getIdPropiedad()
        );

        return repository.save(precio);
    }

    // ELIMINAR
    public void eliminar(Long id) {

        Precio precio = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Precio no encontrado"
                        ));

        repository.delete(precio);
    }

    // Buscar precios por temporada
    public List<Precio> buscarPorTemporada(
            String temporada) {

        List<Precio> precios =
                repository.findByTemporada(
                        temporada
                );

        if (precios.isEmpty()) {

            throw new RuntimeException(
                    "No existen precios para esta temporada"
            );
        }

        return precios;
    }

    // Buscar precios por propiedad
    public List<Precio> buscarPorPropiedad(
            Long idPropiedad) {

        List<Precio> precios =
                repository.findByIdPropiedad(
                        idPropiedad
                );

        if (precios.isEmpty()) {

            throw new RuntimeException(
                    "No existen precios para esta propiedad"
            );
        }

        return precios;
    }

    // Buscar precios por temporada ordenados de mayor a menor
    public List<Precio> buscarPorTemporadaOrdenado(
            String temporada) {

        List<Precio> precios =
                repository
                        .findByTemporadaOrderByMultiplicadorDesc(
                                temporada
                        );

        if (precios.isEmpty()) {

            throw new RuntimeException(
                    "No existen precios ordenados para esta temporada"
            );
        }

        return precios;
    }

}

