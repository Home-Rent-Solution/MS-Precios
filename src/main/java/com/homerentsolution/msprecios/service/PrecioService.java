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

        response.setIdPrecios(guardado.getIdPrecios());
        response.setTemporada(guardado.getTemporada());
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
        return repository.findById(id).orElse(null);
    }

    // ACTUALIZAR
    public Precio actualizar(Long id, Precio precioActualizado) {

        Precio precio = repository.findById(id).orElse(null);

        if (precio != null) {

            precio.setTemporada(precioActualizado.getTemporada());
            precio.setMultiplicador(precioActualizado.getMultiplicador());
            precio.setIdPropiedad(precioActualizado.getIdPropiedad());

            return repository.save(precio);
        }
        return null;
    }

    // ELIMINAR
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    // Buscar precios por temporada
    public List<Precio> buscarPorTemporada(String temporada) {

        return repository.findByTemporada(temporada);
    }

    // Buscar precios por propiedad
    public List<Precio> buscarPorPropiedad(Long idPropiedad) {

        return repository.findByIdPropiedad(idPropiedad);
    }

    // Buscar precios por temporada ordenados de mayor a menor
    public List<Precio> buscarPorTemporadaOrdenado(
            String temporada) {

        return repository
                .findByTemporadaOrderByMultiplicadorDesc(
                        temporada
                );
    }



}

