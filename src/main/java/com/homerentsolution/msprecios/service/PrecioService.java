package com.homerentsolution.msprecios.service;

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

    // GUARDAR
    public Precio guardar(Precio precio) {

        // Regla de negocio
        //Validar que el multiplicador no sea menor a 1
        if (precio.getMultiplicador() < 1) {
            throw new RuntimeException("El multiplicador no puede ser menor a 1");
        }

        // Validar temporadas válidas- alta,media,baja
        if (!precio.getTemporada().equalsIgnoreCase("alta") &&
                !precio.getTemporada().equalsIgnoreCase("media") &&
                !precio.getTemporada().equalsIgnoreCase("baja")) {

            throw new RuntimeException("Temporada inválida");
        }

        return repository.save(precio);
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
}

